eventCompileStart = {
    def rubyDestDir = "${grailsSettings.projectWarExplodedDir}/WEB-INF/ruby"    
    ant.mkdir dir: rubyDestDir    
    ant.copy (todir: rubyDestDir) {
        fileset(dir:"${basedir}/src/ruby", includes:"*.rb")
    }
} 
