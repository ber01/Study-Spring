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

## 2. 스프링 컨테이너(IoC Container)
스프링 컨테이너 : 빈의 생성, 소멸등 빈들을 관리하는 도구
- `ApplicationContext` 를 이용한 모든 `Bean` 확인
  ```Java
  public class BootApplicationTests {

    @Autowired
    ApplicationContext context;

    @Test
    public void confirmBeans(){
        String[] beans = context.getBeanDefinitionNames();
        System.out.println(Arrays.toString(beans));
    }
  }
  ```
  ```Java
  >>> [... , bootApplication, ..., boardController, boardRepository, personController, personRepository, ...]
  ```
  1. `IoC Container` 의 구현체인 `ApplicationContext` 를 주입받는다.
  2. `context.getBeanDefinitionNames()` : `Bean` 으로 등록되어 있는 모든 이름을 가져온다.
- `ApplicationContext` 를 이용하여 특정 `Bean` 조회 및 `null` 확인
  ```Java
  public class BootApplicationTests {

    @Autowired
    ApplicationContext context;

    @Test
    public void getBean(){
        BoardController boardController = (BoardController) context.getBean("boardController");
        BoardRepository boardRepository = context.getBean(BoardRepository.class);

        PersonController personController = context.getBean(PersonController.class);
        PersonRepository personRepository = (PersonRepository) context.getBean("personRepository");

        assertThat(boardController).isNotNull();
        assertThat(boardRepository).isNotNull();
        assertThat(personController).isNotNull();
        assertThat(personRepository).isNotNull();
    }
  }
  ```
  ```Java
  >>> 테스트 성공
  ```
  1. `context.getBean()` 의 매개변수 >>  `Bean` 의 이름 or `Bean` 으로 등록 된 클래스
  2. `Bean` 은 `IoC Container` 에 의하여 객체가 생성 되었기 때문에 `null` 이 아니다.

## 3. 의존성 주입(Dependency Injection) 방법
- 생성자
  ```Java
  @Controller
  public class StudentAController {

    private final StudentARepository studentARepository;

    public StudentAController(StudentARepository studentARepository){
        this.studentARepository = studentARepository;
    }
  }
  ```
  ```Java

  public class StudentControllerTest {

    @Autowired
    StudentAController studentAController;

    @Test
    public void dIConstructor(){
        assertThat(studentAController).isNotNull();
    }
  }
  ```
  ```Java
  >>> 테스트 성공
  ```
- 필드
  ```Java
  @Controller
  public class StudentBController {

    @Autowired
    StudentBRepository studentBRepository;
  }
  ```
  ```Java
  public class StudentControllerTest {

    @Autowired
    StudentBController studentBController;

    @Test
    public void dIField(){
        assertThat(studentBController).isNotNull();
    }
  }
  ```
  ```Java
  >>> 테스트 성공
  ```
- 세터(Setter)
  ```Java
  @Controller
  public class StudentCController {

    private StudentCRepository studentCRepository;

    @Autowired
    public void setStudentCRepository(StudentCRepository studentCRepository) {
        this.studentCRepository = studentCRepository;
    }
  }
  ```
  ```Java
  public class StudentControllerTest {

    @Autowired
    StudentCController studentCController;

    @Test
    public void dISetter(){
        assertThat(studentCController).isNotNull();
    }
  }
  ```
  ```Java
  >>> 테스트 성공
  ```

## 4. 빈(Bean) 등록 방법
