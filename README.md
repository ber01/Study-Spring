# Study-Spring

## 1. 제어권의 역전(Inversion of Control)
- 일반적인 제어
  ```Java
  @Controller
  public class BoardController {

      private final BoardRepository boardRepository = new BoardRepository();

      public void save(){
          boardRepository.save();
      }
  }
  ```
  ```Java
  @Repository
  public class BoardRepository {

    public void save() {
        System.out.println("BoardRepository save method 실행");
    }
  }
  ```
  ```Java
  public class BoardControllerTest {

    @Test
    public void controllerTest(){
        BoardController boardController = new BoardController();
        boardController.save();
    }
  }
  ```
  ```Java
  >>> BoardRepository save method 실행
  ```
  1. `BoardController` 클래스의 `save` 메소드를 사용하기 위해서는 `BoardRepository` 객체가 필요하다.
  2. 필요한 의존 객체(`BoardRepository`)를 자신이 만들어(`new`) 사용한다.
- 제어권의 역전
  ```Java
  @Controller
  public class PersonController {

    private final PersonRepository personRepository;

    public PersonController(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public void save(){
        personRepository.save();
    }
  }
  ```
  ```Java
  @Repository
  public class PersonRepository {

    public void save() {
        System.out.println("PersonRepository save method 실행");
    }
  }
  ```
  ```Java
  public class PersonControllerTest {

    @Test
    public void controllerTest(){
        PersonRepository personRepository = new PersonRepository();
        PersonController personController = new PersonController(personRepository);
        personController.save();
    }
  }
  ```
  ```Java
  >>> PersonRepository save method 실행
  ```
  1. `PersonController` 의 `save` 메소드를 사용하기 위해서는 `PersonRepository` 객체가 필요하다.
  2. 필요한 의존 객체(`PersonRepository`)를 자신이 아닌 외부(`PersonControllerTest`)에서 만들어 주입한다.

## 2. 빈(Bean)과 IoC Container
빈 : IoC Container 가 관리하는 자바 클래스
IoC Container : 빈의 생성, 소멸등 빈을 관리하는 장치
