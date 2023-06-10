package com.zhangke.filt.compiler

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.processing.SymbolProcessorProvider
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterSpec
import com.squareup.kotlinpoet.TypeSpec
import com.zhangke.filt.annotaions.Filt
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoSet

class FiltProcessorProvider : SymbolProcessorProvider {

    override fun create(environment: SymbolProcessorEnvironment): SymbolProcessor {
        return FiltProcessor(environment)
    }
}

class FiltProcessor(private val environment: SymbolProcessorEnvironment) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        resolver.getSymbolsWithAnnotation(Filt::class.qualifiedName!!)
            .map { it as KSClassDeclaration }
            .toList()
            .forEach { it.accept(FiltVisitor(environment.logger, environment.codeGenerator), Unit) }
        return emptyList()
    }
}

class FiltVisitor(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator,
) : KSVisitorVoid() {

    private val badTypeName = Unit::class.qualifiedName
    private val badSuperTypeName = Any::class.qualifiedName

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val targetInterfaceType = findSuperInterfaceType(classDeclaration)
        val installInComponent = findInstallInComponent(classDeclaration)

        generateDaggerModuleClass(
            packageName = classDeclaration.packageName.asString(),
            className = classDeclaration.simpleName.asString(),
            superTypeName = targetInterfaceType,
            installInTypeName = installInComponent,
        )
    }

    private fun findSuperInterfaceType(classDeclaration: KSClassDeclaration): String {
        val className = classDeclaration.qualifiedName?.asString().orEmpty()
        val filtAnnotation = classDeclaration.requireAnnotation<Filt>()
        val typeFromAnnotation = filtAnnotation.findArgumentTypeByName("type")
            ?.takeIf { it != badTypeName }
        if (typeFromAnnotation != null) {
            val superTypesIterator = classDeclaration.superTypes.iterator()
            var find = false
            while (superTypesIterator.hasNext()) {
                val type = superTypesIterator.next()
                if (type.typeQualifiedName == typeFromAnnotation) {
                    find = true
                    break
                }
            }
            if (!find) {
                val errorMessage = "Can't find $typeFromAnnotation from $className super type!"
                logger.error(errorMessage)
                throw IllegalArgumentException(errorMessage)
            }
            return typeFromAnnotation
        }
        if (classDeclaration.superTypes.isSingleElement()) {
            val superTypeName = classDeclaration.superTypes
                .iterator()
                .next()
                .typeQualifiedName
                ?.takeIf { it != badSuperTypeName }
            if (!superTypeName.isNullOrEmpty()) {
                return superTypeName
            }
        }
        val errorMessage = "Can't find Filt type from $className"
        logger.error(errorMessage)
        throw IllegalArgumentException(errorMessage)
    }

    private fun findInstallInComponent(classDeclaration: KSClassDeclaration): String {
        val filtAnnotation = classDeclaration.requireAnnotation<Filt>()
        val installInFromAnnotation = filtAnnotation.findArgumentTypeByName("installIn")
            ?.takeIf { it != badTypeName }
        if (installInFromAnnotation != null) return installInFromAnnotation
        return "dagger.hilt.android.components.ActivityComponent"
    }

    private fun generateDaggerModuleClass(
        packageName: String,
        className: String,
        superTypeName: String,
        installInTypeName: String,
    ) {
        val generateClassName = className + "BindModule"
        val fileSpec = FileSpec.builder(
            packageName = packageName,
            fileName = className,
        ).apply {
            val (superPackage, superClassName) = ClassNameUtils.splitPackageAndName(superTypeName)
            addType(
                TypeSpec.classBuilder(ClassName(packageName, generateClassName))
                    .addModifiers(KModifier.ABSTRACT)
                    .addAnnotation(installInAnnotations(installInTypeName))
                    .addAnnotation(Module::class)
                    .addFunction(
                        FunSpec.builder("bind")
                            .addAnnotation(Binds::class)
                            .addAnnotation(IntoSet::class)
                            .addModifiers(KModifier.ABSTRACT)
                            .addParameter(
                                ParameterSpec.builder(
                                    name = "input",
                                    type = ClassName(packageName, className),
                                ).build()
                            )
                            .returns(ClassName(superPackage, superClassName))
                            .build()
                    )
                    .build()
            )
        }.build()

        codeGenerator.createNewFile(
            dependencies = Dependencies(aggregating = false),
            packageName = packageName,
            fileName = generateClassName,
        ).use { os ->
            os.writer().use { fileSpec.writeTo(it) }
        }
    }
}
