import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import org.springframework.core.io.FileSystemResource

class RubyGrailsPlugin {
    // the plugin version
    def version = "1.0.M1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.2 > *"
    // the other plugins this plugin depends on
    def dependsOn = [:]

    def title = "Ruby" // Headline display name of the plugin
    def author = "Bobby Warner"
    def authorEmail = "bobbywarner@gmail.com"
    def description = "Plugin for using Ruby code in Grails via JRuby."
    def documentation = "http://grails.org/plugin/ruby"
    def license = "APACHE"
    
    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPRUBY" ]
    def scm = [ url: "https://github.com/bobbywarner/grails-ruby" ]

    def watchedResources = "file:./src/ruby/*.rb"
    
    def onChange = { event ->
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("jruby")
        
        def source = event.source
        if(source instanceof FileSystemResource && source.file.name.endsWith('.rb')) {
            source.file.withReader { reader ->
                engine.eval(reader);
            }
        }
    }
    
    def doWithDynamicMethods = { ctx ->
        ScriptEngine engine = new ScriptEngineManager().getEngineByName("jruby")
        
        def rubyFiles
        if(application.warDeployed) {
            rubyFiles = parentCtx?.getResources("**/WEB-INF/ruby/*.rb")?.toList()
        } else {
            rubyFiles = plugin.watchedResources
        }
        
        rubyFiles.each {
            it.file.withReader { reader ->
                engine.eval(reader)
            }
        }
    
        application.allClasses*.metaClass*."getRuby" = {
            return engine
        }
    }
}
