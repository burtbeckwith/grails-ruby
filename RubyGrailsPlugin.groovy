import javax.script.ScriptEngine
import javax.script.ScriptEngineManager
import org.springframework.core.io.FileSystemResource

class RubyGrailsPlugin {
    def version = "1.0.M1"
    def grailsVersion = "1.2 > *"
    def title = "Ruby"
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
        if(source instanceof FileSystemResource && source.file.name.endsWith('.rb')) {
            ScriptEngine engine = new ScriptEngineManager().getEngineByName("jruby")
            source.file.withReader { reader ->
                engine.eval(reader)
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
