class RubyGrailsPlugin {
    // the plugin version
    def version = "1.0.M1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "2.0 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    def title = "Ruby Plugin" // Headline display name of the plugin
    def author = "Bobby Warner"
    def authorEmail = "bobbywarner@gmail.com"
    def description = "Plugin for using Ruby code in Grails via JRuby."
    def documentation = "http://grails.org/plugin/ruby"
    def license = "APACHE"
    
    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPRUBY" ]
    def scm = [ url: "https://github.com/bobbywarner/grails-ruby" ]

    def watchedResources = "file:./src/ruby/*.rb"
    
    def onChange = { event ->
        def source = event.source
        if(source instanceof org.springframework.core.io.FileSystemResource && source.file.name.endsWith('.rb')) {
            source.file.withReader { reader ->
                javax.script.ScriptEngine jruby = new javax.script.ScriptEngineManager().getEngineByName("jruby");
                jruby.compile(reader);
            }
        }
    }
}
