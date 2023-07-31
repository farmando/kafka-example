package org.gradle.accessors.dm;

import org.gradle.api.NonNullApi;
import org.gradle.api.artifacts.MinimalExternalModuleDependency;
import org.gradle.plugin.use.PluginDependency;
import org.gradle.api.artifacts.ExternalModuleDependencyBundle;
import org.gradle.api.artifacts.MutableVersionConstraint;
import org.gradle.api.provider.Provider;
import org.gradle.api.model.ObjectFactory;
import org.gradle.api.provider.ProviderFactory;
import org.gradle.api.internal.catalog.AbstractExternalDependencyFactory;
import org.gradle.api.internal.catalog.DefaultVersionCatalog;
import java.util.Map;
import org.gradle.api.internal.attributes.ImmutableAttributesFactory;
import org.gradle.api.internal.artifacts.dsl.CapabilityNotationParser;
import javax.inject.Inject;

/**
 * A catalog of dependencies accessible via the `libs` extension.
 */
@NonNullApi
public class LibrariesForLibs extends AbstractExternalDependencyFactory {

    private final AbstractExternalDependencyFactory owner = this;
    private final JunitLibraryAccessors laccForJunitLibraryAccessors = new JunitLibraryAccessors(owner);
    private final KafakaLibraryAccessors laccForKafakaLibraryAccessors = new KafakaLibraryAccessors(owner);
    private final OpenLibraryAccessors laccForOpenLibraryAccessors = new OpenLibraryAccessors(owner);
    private final SpringLibraryAccessors laccForSpringLibraryAccessors = new SpringLibraryAccessors(owner);
    private final SpringbootLibraryAccessors laccForSpringbootLibraryAccessors = new SpringbootLibraryAccessors(owner);
    private final VersionAccessors vaccForVersionAccessors = new VersionAccessors(providers, config);
    private final BundleAccessors baccForBundleAccessors = new BundleAccessors(objects, providers, config, attributesFactory, capabilityNotationParser);
    private final PluginAccessors paccForPluginAccessors = new PluginAccessors(providers, config);

    @Inject
    public LibrariesForLibs(DefaultVersionCatalog config, ProviderFactory providers, ObjectFactory objects, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) {
        super(config, providers, objects, attributesFactory, capabilityNotationParser);
    }

    /**
     * Returns the group of libraries at junit
     */
    public JunitLibraryAccessors getJunit() {
        return laccForJunitLibraryAccessors;
    }

    /**
     * Returns the group of libraries at kafaka
     */
    public KafakaLibraryAccessors getKafaka() {
        return laccForKafakaLibraryAccessors;
    }

    /**
     * Returns the group of libraries at open
     */
    public OpenLibraryAccessors getOpen() {
        return laccForOpenLibraryAccessors;
    }

    /**
     * Returns the group of libraries at spring
     */
    public SpringLibraryAccessors getSpring() {
        return laccForSpringLibraryAccessors;
    }

    /**
     * Returns the group of libraries at springboot
     */
    public SpringbootLibraryAccessors getSpringboot() {
        return laccForSpringbootLibraryAccessors;
    }

    /**
     * Returns the group of versions at versions
     */
    public VersionAccessors getVersions() {
        return vaccForVersionAccessors;
    }

    /**
     * Returns the group of bundles at bundles
     */
    public BundleAccessors getBundles() {
        return baccForBundleAccessors;
    }

    /**
     * Returns the group of plugins at plugins
     */
    public PluginAccessors getPlugins() {
        return paccForPluginAccessors;
    }

    public static class JunitLibraryAccessors extends SubDependencyFactory {

        public JunitLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for jupiter (org.junit.jupiter:junit-jupiter)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getJupiter() {
                return create("junit.jupiter");
        }

    }

    public static class KafakaLibraryAccessors extends SubDependencyFactory {
        private final KafakaTestLibraryAccessors laccForKafakaTestLibraryAccessors = new KafakaTestLibraryAccessors(owner);

        public KafakaLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

        /**
         * Returns the group of libraries at kafaka.test
         */
        public KafakaTestLibraryAccessors getTest() {
            return laccForKafakaTestLibraryAccessors;
        }

    }

    public static class KafakaTestLibraryAccessors extends SubDependencyFactory {

        public KafakaTestLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for container (org.testcontainers:kafka)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getContainer() {
                return create("kafaka.test.container");
        }

    }

    public static class OpenLibraryAccessors extends SubDependencyFactory {

        public OpenLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for api (org.springdoc:springdoc-openapi-starter-webmvc-ui)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getApi() {
                return create("open.api");
        }

    }

    public static class SpringLibraryAccessors extends SubDependencyFactory {

        public SpringLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for kafka (org.springframework.kafka:spring-kafka)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getKafka() {
                return create("spring.kafka");
        }

    }

    public static class SpringbootLibraryAccessors extends SubDependencyFactory {
        private final SpringbootStarterLibraryAccessors laccForSpringbootStarterLibraryAccessors = new SpringbootStarterLibraryAccessors(owner);

        public SpringbootLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for actuator (org.springframework.boot:spring-boot-starter-actuator)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getActuator() {
                return create("springboot.actuator");
        }

            /**
             * Creates a dependency provider for retry (org.springframework.retry:spring-retry)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getRetry() {
                return create("springboot.retry");
        }

        /**
         * Returns the group of libraries at springboot.starter
         */
        public SpringbootStarterLibraryAccessors getStarter() {
            return laccForSpringbootStarterLibraryAccessors;
        }

    }

    public static class SpringbootStarterLibraryAccessors extends SubDependencyFactory {

        public SpringbootStarterLibraryAccessors(AbstractExternalDependencyFactory owner) { super(owner); }

            /**
             * Creates a dependency provider for test (org.springframework.boot:spring-boot-starter-test)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getTest() {
                return create("springboot.starter.test");
        }

            /**
             * Creates a dependency provider for validation (org.springframework.boot:spring-boot-starter-validation)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getValidation() {
                return create("springboot.starter.validation");
        }

            /**
             * Creates a dependency provider for web (org.springframework.boot:spring-boot-starter-web)
             * This dependency was declared in catalog libs.versions.toml
             */
            public Provider<MinimalExternalModuleDependency> getWeb() {
                return create("springboot.starter.web");
        }

    }

    public static class VersionAccessors extends VersionFactory  {

        private final FreefairVersionAccessors vaccForFreefairVersionAccessors = new FreefairVersionAccessors(providers, config);
        private final JunitVersionAccessors vaccForJunitVersionAccessors = new JunitVersionAccessors(providers, config);
        private final OpenVersionAccessors vaccForOpenVersionAccessors = new OpenVersionAccessors(providers, config);
        private final SpringVersionAccessors vaccForSpringVersionAccessors = new SpringVersionAccessors(providers, config);
        private final SpringbootVersionAccessors vaccForSpringbootVersionAccessors = new SpringbootVersionAccessors(providers, config);
        public VersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: testcontainers (1.18.3)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getTestcontainers() { return getVersion("testcontainers"); }

        /**
         * Returns the group of versions at versions.freefair
         */
        public FreefairVersionAccessors getFreefair() {
            return vaccForFreefairVersionAccessors;
        }

        /**
         * Returns the group of versions at versions.junit
         */
        public JunitVersionAccessors getJunit() {
            return vaccForJunitVersionAccessors;
        }

        /**
         * Returns the group of versions at versions.open
         */
        public OpenVersionAccessors getOpen() {
            return vaccForOpenVersionAccessors;
        }

        /**
         * Returns the group of versions at versions.spring
         */
        public SpringVersionAccessors getSpring() {
            return vaccForSpringVersionAccessors;
        }

        /**
         * Returns the group of versions at versions.springboot
         */
        public SpringbootVersionAccessors getSpringboot() {
            return vaccForSpringbootVersionAccessors;
        }

    }

    public static class FreefairVersionAccessors extends VersionFactory  {

        public FreefairVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: freefair.lombok (8.1.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getLombok() { return getVersion("freefair.lombok"); }

    }

    public static class JunitVersionAccessors extends VersionFactory  {

        public JunitVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: junit.jupiter (5.9.3)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getJupiter() { return getVersion("junit.jupiter"); }

    }

    public static class OpenVersionAccessors extends VersionFactory  {

        public OpenVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: open.api (2.0.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getApi() { return getVersion("open.api"); }

    }

    public static class SpringVersionAccessors extends VersionFactory  {

        private final SpringBootVersionAccessors$1 vaccForSpringBootVersionAccessors$1 = new SpringBootVersionAccessors$1(providers, config);
        private final SpringDependencyVersionAccessors vaccForSpringDependencyVersionAccessors = new SpringDependencyVersionAccessors(providers, config);
        public SpringVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: spring.kafka (3.0.8)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getKafka() { return getVersion("spring.kafka"); }

        /**
         * Returns the group of versions at versions.spring.boot
         */
        public SpringBootVersionAccessors$1 getBoot() {
            return vaccForSpringBootVersionAccessors$1;
        }

        /**
         * Returns the group of versions at versions.spring.dependency
         */
        public SpringDependencyVersionAccessors getDependency() {
            return vaccForSpringDependencyVersionAccessors;
        }

    }

    public static class SpringBootVersionAccessors$1 extends VersionFactory  {

        public SpringBootVersionAccessors$1(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: spring.boot.plugin (3.1.1)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getPlugin() { return getVersion("spring.boot.plugin"); }

    }

    public static class SpringDependencyVersionAccessors extends VersionFactory  {

        public SpringDependencyVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: spring.dependency.manager (1.1.0)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getManager() { return getVersion("spring.dependency.manager"); }

    }

    public static class SpringbootVersionAccessors extends VersionFactory  {

        public SpringbootVersionAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Returns the version associated to this alias: springboot.retry (2.0.2)
             * If the version is a rich version and that its not expressible as a
             * single version string, then an empty string is returned.
             * This version was declared in catalog libs.versions.toml
             */
            public Provider<String> getRetry() { return getVersion("springboot.retry"); }

    }

    public static class BundleAccessors extends BundleFactory {

        public BundleAccessors(ObjectFactory objects, ProviderFactory providers, DefaultVersionCatalog config, ImmutableAttributesFactory attributesFactory, CapabilityNotationParser capabilityNotationParser) { super(objects, providers, config, attributesFactory, capabilityNotationParser); }

    }

    public static class PluginAccessors extends PluginFactory {
        private final FreefairPluginAccessors paccForFreefairPluginAccessors = new FreefairPluginAccessors(providers, config);
        private final SpringPluginAccessors paccForSpringPluginAccessors = new SpringPluginAccessors(providers, config);

        public PluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of plugins at plugins.freefair
         */
        public FreefairPluginAccessors getFreefair() {
            return paccForFreefairPluginAccessors;
        }

        /**
         * Returns the group of plugins at plugins.spring
         */
        public SpringPluginAccessors getSpring() {
            return paccForSpringPluginAccessors;
        }

    }

    public static class FreefairPluginAccessors extends PluginFactory {

        public FreefairPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for freefair.lombok to the plugin id 'io.freefair.lombok'
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getLombok() { return createPlugin("freefair.lombok"); }

    }

    public static class SpringPluginAccessors extends PluginFactory {
        private final SpringBootPluginAccessors paccForSpringBootPluginAccessors = new SpringBootPluginAccessors(providers, config);
        private final SpringDependencyPluginAccessors paccForSpringDependencyPluginAccessors = new SpringDependencyPluginAccessors(providers, config);

        public SpringPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

        /**
         * Returns the group of plugins at plugins.spring.boot
         */
        public SpringBootPluginAccessors getBoot() {
            return paccForSpringBootPluginAccessors;
        }

        /**
         * Returns the group of plugins at plugins.spring.dependency
         */
        public SpringDependencyPluginAccessors getDependency() {
            return paccForSpringDependencyPluginAccessors;
        }

    }

    public static class SpringBootPluginAccessors extends PluginFactory {

        public SpringBootPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for spring.boot.plugin to the plugin id 'org.springframework.boot'
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getPlugin() { return createPlugin("spring.boot.plugin"); }

    }

    public static class SpringDependencyPluginAccessors extends PluginFactory {

        public SpringDependencyPluginAccessors(ProviderFactory providers, DefaultVersionCatalog config) { super(providers, config); }

            /**
             * Creates a plugin provider for spring.dependency.manager to the plugin id 'io.spring.dependency-management'
             * This plugin was declared in catalog libs.versions.toml
             */
            public Provider<PluginDependency> getManager() { return createPlugin("spring.dependency.manager"); }

    }

}
