
class MyTest{

    @Test
    void simple() {
        String str = "스프링 부트에서도 JUNIT 잘 돌아가네요!"
        assertEquals "스프링 부트에서도 JUNIT 잘 돌아가네요!", str
    }
}
