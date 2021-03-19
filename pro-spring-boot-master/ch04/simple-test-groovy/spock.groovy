@Grab('org.spockframework:spock-core:1.0-groovy-2.4')
import spock.lang.Specification
import org.springframework.boot.test.OutputCapture

class SimpleSpockTest extends Specification {

    @org.junit.Rule
    OutputCapture capture = new OutputCapture()


    def "get output and capture it"() {
        when:
        print '스프링 부트와 스팍의 만남!'

        then:
        capture.toString() == '스프링 부트와 스팍의 만남!'
    }

}

