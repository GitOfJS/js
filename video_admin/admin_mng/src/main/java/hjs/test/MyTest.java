package hjs.test;

public class MyTest {

    public void test(){
        Test test = new Test();

    }

    public class Test{
        public void out(){
            System.out.println("111");
        }
    }
}

class Main{
    public static void main(String[] args) {
        MyTest test = new MyTest();
        MyTest.Test test1 = test.new Test();
    }
}
