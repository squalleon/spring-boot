# 스프링 부트 CLI - 초기화 예제

스프링 부트 프로젝트는 다음과 같이 초기화합니다.
```bash
$ spring init  --build gradle myapp
```

웹이나 JPA 같은 다른 기능을 추가하려면,
```bash
$ spring init -dweb,data-jpa,h2,thymeleaf --build maven myapp
```

___spring___ 명령어를 실행하면 가능한 모든 옵션을 볼 수 있습니다.
 ```
$ spring
usage: spring [--help] [--version]
       <command> [<args>]

Available commands are:

  run [options] <files> [--] [args]
    Run a spring groovy script

  test [options] <files> [--] [args]
    Run a spring groovy script test

  grab
    Download a spring groovy scripts dependencies to ./repository

  jar [options] <jar-name> <files>
    Create a self-contained executable jar file from a Spring Groovy script

  war [options] <war-name> <files>
    Create a self-contained executable war file from a Spring Groovy script

  install [options] <coordinates>
    Install dependencies to the lib directory

  uninstall [options] <coordinates>
    Uninstall dependencies from the lib directory

  init [options] [location]
    Initialize a new project using Spring Initializr (start.spring.io)

  shell
    Start a nested shell

Common options:

  -d, --debug Verbose mode
    Print additional status information for the command you are running


'spring help <명령어>'하면 해당 명령어에 관한 자세한 정보를 볼 수 있습니다.
 ```
