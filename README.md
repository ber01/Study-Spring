# Study-Spring

## 1. 제어권의 역전(Inversion of Control)
<details markdown="1">
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
</details>

## 2. 스프링 컨테이너(IoC Container)
<details markdown="1">
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
</details>

## 3. 의존성 주입(Dependency Injection) 방법
<details markdown="1">
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
</details>

## 4. 빈(Bean) 등록
<details markdown="1">
빈 : IoC Container 에서 관리하는 자바 객체
- XML 이용 - 1 (`<bean>`)
  ```Java
  public class UserController {

    UserRepository userRepository;

    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
  }
  ```
  ```Java
  public class UserRepository {
  }
  ```
  ```Java
  public class BootApplication {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("application.xml");
        String[] beans = context.getBeanDefinitionNames();

        System.out.println(Arrays.toString(beans));
    }
  }
  ```
  ```Java
  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean id="userController" class="com.kyunghwan.User.UserController">
        <property name="userRepository" ref="userRepository"/>
    </bean>

    <bean id="userRepository" class="com.kyunghwan.User.UserRepository"/>
  </beans>
  ```
  ```Java
  >>> [userController, userRepository]
  ```
  1. `Bean` 의 속성으로 `id`, `class` 를 지정한다.
  2. `<property>` 태그를 이용하여 의존 객체(`userRepository`)를 주입한다.
  3. IoC Container 를 이용하여 `Bean` 을 출력하면 XML에서 `Bean` 으로 등록하였던 객체가 출력된다.

- XML 이용 - 2 (`context:component-scan`)
  ```Java
  @Controller // 추가
  public class UserController {
    // 동일
  }
  ```
  ```Java
  @Repository // 추가
  public class UserRepository {
  }
  ```
  ```Java
  public class BootApplication {

    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("application2.xml");
        String[] beans = context.getBeanDefinitionNames();

        System.out.println(Arrays.toString(beans));
    }
  }
  ```
  ```Java
  <?xml version="1.0" encoding="UTF-8"?>
  <beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">

    <context:component-scan base-package="com.kyunghwan"/>

  </beans>
  ```
  ```Java
  >>> [userController, userRepository, ..., ...]
  ```
  1. `@Component`이 포함된 클래스를 전부 `Bean` 으로 등록한다.
  2. Controller와 Repository에 `@Component` 어노테이션을 확장한 `@Controller`, `@Repository` 어노테이션을 추가하였기 때문에 `Bean` 으로 등록된다.

- Java 클래스 이용 - 1 (`@Configuration`, `@bean`)
  ```Java
  // 삭제
  public class UserController {
    // 동일
  }
  ```
  ```Java
  // 삭제
  public class UserRepository {
  }
  ```
  ```Java
  @Configuration
  public class UserConfig {

    @Bean
    public UserRepository userRepository(){
        return new UserRepository();
    }

    @Bean
    public UserController userController(UserRepository userRepository){
        UserController userController = new UserController();
        userController.setUserRepository(userRepository);
        return new UserController();
    }
  }
  ```
  ```Java
  public class BootApplication {

    public static void main(String[] args) {

        ApplicationContext context = new AnnotationConfigApplicationContext(UserConfig.class);
        String[] beans = context.getBeanDefinitionNames();

        System.out.println(Arrays.toString(beans));
    }
  }
  ```
  ```Java
  >>> [..., ..., userConfig, userRepository, userController]
  ```  
  1. `Bean` 설정 파일 클래스(`UserConfig`)에 `@Configuration` 어노테이션 추가
  2. `@Bean` 어노테이션을 사용하여 `Bean` 으로 등록할 객체 반환
  3. `AnnotationConfigApplicationContext` 의 매개변수에 `@Configuration` 을 적용한 클래스를 사용하여 해당 클래스가 빈 설정 파일이 된다.

- Java 클래스 이용 - 2 (`@ComponentScan`)
  ```Java
  @Configuration
  @ComponentScan(basePackageClasses = com.kyunghwan.User.UserConfig.class)
  public class UserConfig {
  }
  ```
  ```Java
  @Controller // 추가
  public class UserController {
    // 동일
  }
  ```
  ```Java
  @Repository // 추가
  public class UserRepository {
  }
  ```
  ```Java
  public class BootApplication {

    public static void main(String[] args) {
      // 동일
  }
  ```
  ```Java
  >>> [..., ..., userConfig, userRepository, userController]
  ```
  1. `@Component` 어노테이션을 이용하여 `@Component` 어노테이션을 사용하는 모든 클래스를 `Bean` 으로 등록
  2. `basePackageClasses` 속성으로 탐색 시작 클래스 설정
  3. `@Component` 어노테이션을 포함하는 `UserConfig`, `UserController`, `UserRepository` 클래스 `Bean` 으로 등록

- Java 클래스 이용 - 3 (`@SpringBootApplication`)
  ```Java
  @SpringBootApplication
  public class BootApplication {

    public static void main(String[] args) {
        SpringApplication.run(BootApplication.class, args);
    }
  }
  ```
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
  >>> [..., ..., UserController, UserRepository, ...]
  ```
  1. `@SpringBootApplication` 은 `@ComponentScan`, `@Configuration` 두 가지의 어노테이션을 포함한다.
  2. `BootApplication` 클래스 자체가 `Bean` 설정 파일이 된다.
</details>
