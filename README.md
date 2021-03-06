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
  3. IoC Container(`ApplicationContext`) 를 이용하여 `Bean` 을 출력하면 XML에서 `Bean` 으로 등록하였던 객체가 출력된다.

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

## 5. @Autowired

<details markdown="1">

`@Autowired` : `Bean` 으로 등록된 클래스들의 의존성을 주입시켜주는 어노테이션

- 필드를 통한 의존성 주입
  ```Java
  @Controller
  public class ShopController {

    @Autowired
    private ShopRepository shopRepository;
  }
  ```

  ```Java
  public class ShopRepository {
  }
  ```

  ```Java
  >>> 어플리케이션 실행 실패
  ```

  1. `ShopController` 클래스는 `@Controller` 어노테이션을 사용하고 있으므로 `Bean` 이다.
  2. `ShopRepository` 클래스는 `@Component` 어노테이션을 포함하지 않으므로 `Bean` 이 아니다.
  3. `@Autowired` 로 의존성을 주입 받기 위해서는 의존 관계의 클래스 들은 모두 `Bean` 으로 등록 되어야 한다.
  4. `ShopController` 클래스의 인스턴스는 생성이 되지만 의존성 주입에 실패하여 어플리케이션 실행이 실패한다.

  ```Java
  @Repository // 추가
  public class ShopRepository {
  }
  ```

  ```Java
  >>> 어플리케이션 실행 성공
  ```

  1. `ShopRepository` 클래스에 `@Repository` 어노테이션을 사용하여 `Bean` 으로 등록한다.
  2. 의존 관계에 있는 클래스들이 모두 `Bean` 이므로 의존성 주입이 성공하여 어플리케이션 실행이 성공한다.

- 선택적인 의존성 주입

  ```Java
  @Controller
  public class ShopController {

      @Autowired(required = false)
      private ShopRepository shopRepository;
  }
  ```

  ```Java
  // 삭제
  public class ShopRepository {
  }
  ```

  ```Java
  >>> 어플리케이션 실행 성공
  ```

  1. `required = false` 를 사용하면 의존성 주입을 선택적으로 할 수 있다.
  2. `ShopController` 클래스는 의존성이 주입되지 않은 채 `Bean` 으로 등록된다.
  3. `ShopRepository` 클래스는 `Bean` 이 아니지만 의존성 주입을 하지 않았기에 어플리케이션 실행이 성공한다.

- 생성자를 통한 의존성 주입

  ```Java
  @Controller
  public class ShopController {

      private ShopRepository shopRepository;

      @Autowired(required = false)
      public ShopController(ShopRepository shopRepository){
          this.shopRepository = shopRepository;
      }
  }
  ```

  ```Java
  >>> 어플리케이션 실행 실패
  ```

  1. 생성자를 통하여 의존성을 주입 받을 경우 `ShopController` 클래스는 `ShopRepository` 객체가 없으면 생성이 불가능하다.
  2. 의존성 주입을 선택적으로 할 수 없다.
  3. 필요한 의존 객체를 강제 할 수 있는 장점을 가진다.

- 같은 타입의 `Bean` 이 여러개 일 경우

  ```Java
  public interface ShopInterfaceRepository {
  }
  ```

  ```Java
  @Repository
  public class ShopAbcRepository implements ShopInterfaceRepository {
  }
  ```

  ```Java
  @Repository
  public class ShopDefRepository implements ShopInterfaceRepository {
  }
  ```

  ```Java
  @Controller
  public class ShopController {

      @Autowired
      ShopInterfaceRepository repository;
  }
  ```

  ```Java
  >>> 어플리케이션 실행 실패
  ```

  1. 하나의 인터페이스를 상속받은 두 개의 `Bean` 이 존재하는 경우(같은 타입의 `Bean` 이 다수)
  2. 사용자가 원하는 `Bean` 을 알 수가 없으므로 의존성 주입에 실패한다.

  - 해결방법 1 : `@Primary`

    ```Java
    @Repository @Primary
    public class ShopDefRepository implements ShopInterfaceRepository {
    }
    ```

    ```Java
    @Controller
    public class ShopController {

        @Autowired
        ShopInterfaceRepository repository;

        public void printBean(){
            System.out.println(repository.getClass());
        }
    }
    ```

    ```Java
    public class ShopControllerTest {

        @Autowired
        ShopController shopController;

        @Test
        public void printBean(){
            shopController.printBean();
        }
    }
    ```

    ```Java
    >>> com.kyunghwan.Shop.ShopAbcRepository
    ```

    1. `@Primary` 어노테이션을 사용하면 같은 타입의 `Bean` 이 존재하여도 의존성 주입이 가능하다.
    2. `@Primary` 어노테이션이 적용된 `ShopAbcRepository` 클래스가 출력된다.
  - 해결방법2 : `@Qualifier`
    ```Java
    @Repository // 삭제
    public class ShopDefRepository implements ShopInterfaceRepository {
    }
    ```

    ```Java
    @Controller
    public class ShopController {

        @Autowired @Qualifier("shopAbcRepository")
        ShopInterfaceRepository repository;

        // 동일
    }
    ```

    ```Java
    >>> com.kyunghwan.Shop.ShopAbcRepository
    ```
    1. `@Qualifier` 어노테이션과 `Bean` 의 id를 이용하여 의존성 주입이 가능하다.
    2. `Bean` 의 id는 앞자리가 소문자인 클래스의 이름이다.

  - 해결방법3 : 다수의 `Bean` 모두 주입받기

    ```Java
    @Controller
    public class ShopController {

        @Autowired
        List<ShopInterfaceRepository> repositories;

        public void printBean(){
            for (ShopInterfaceRepository repository : repositories){
                System.out.println(repository.getClass());
            }
        }
    }
    ```

    ```Java
    >>> com.kyunghwan.Shop.ShopAbcRepository
    >>> com.kyunghwan.Shop.ShopDefRepository
    ```

    1. `List` 자료형을 이용하여 해당하는 타입의 모든 `Bean` 을 받아온다.
    2. 타입이 똑같은 `Bean`이 모두 출력된다.

</details>

## 6. Scope

<details markdown="1">

- 모든 `Bean` 은 `Scope` 가 존재한다.
  1. `Scope` 에 관련된 설정을 하지 않으면 기본적으로 `싱글톤` 으로 생성된다.
  2. `싱글톤` : 해당 `Bean` 의 인스턴스가 1개 존재하는 형태
      ```Java
      @Component
      public class Single {
      }
      ```
      ```Java
      @SpringBootTest
      public class SingleTest {
        @Autowired
        Single single;

        @Autowired
        Single single2;

        @Test
        public void 싱글톤_테스트(){

            System.out.println(single); // Single@3eed0f5
            System.out.println(single2); // Single@3eed0f5

            assertThat(single).isEqualTo(single2);
        }
      }
      ```
- `프로토타입`
  1. 새로운 인스턴스를 생성하여 사용하는 형태
      ```Java
      @Component @Scope("prototype")
      public class Proto {
      }
      ```
      ```Java
      public class SingleTest {

        @Autowired
        Proto proto;

        @Autowired
        Proto proto2;

        @Test
        public void 프로토_테스트(){
            System.out.println(proto); // Proto@2dbd803f
            System.out.println(proto2); // Proto@3e48e859

            assertThat(proto).isNotEqualTo(proto2);
        }
      }
      ```
  2. `프로토타입` 의 `Bean` 이 `싱글톤` 을 참조하는 경우 문제가 없다.
- `싱글톤` 이 `프로토타입` 을 참조하는 경우
  1. `프로토타입` 의 `Bean` 이 인스턴스를 생성하지 못하는 상황이 발생
      ```Java
      @Component
      public class Single {
          @Autowired
          Proto proto;
      }
      ```
      ```Java
      @Test
      public void 싱글톤이_프로토_참조(){
          System.out.println(single.getProto()); // Proto@3a022576
          System.out.println(single.getProto()); // Proto@3a022576

          assertThat(single.getProto()).isEqualTo(single.getProto());
      }
      ```
  2. 해결방법, `proxyMode` 속성 사용
     ```Java
     @Component @Scope(value = "prototype", proxyMode = ScopedProxyMode.TARGET_CLASS)
     public class Proto {
     }
     ```
     ```Java
     @Test
     public void 싱글톤이_프로토_참조(){
        System.out.println(single.getProto()); // Proto@78de58ea
        System.out.println(single.getProto()); // Proto@3a022576
     }
     ```


</details>

## 7. Validation

<details markdown="1">

1. `Validator` 사용
    1. [Interface Validator](https://docs.spring.io/spring/docs/current/javadoc-api/org/springframework/validation/Validator.html) : 애플리케이션 고유의 객체에 대한 유효성 검사기
    2. `supports`, `validate` 두 가지의 메소드를 구현하여야 한다.
        - `supprots` : 검증하여야 하는 인스턴스의 클래스가 `Validator` 가 검증할 수 있는지 판단하는 메소드
        - `Event` 클래스의 적합성 검사
          ```java
          @Override
          public boolean supports(Class<?> clazz) {
              return Event.class.equals(clazz);
          }
          ```
        - `validate` : 검증 가능한 클래스에 대한 유효성 검사가 실질적으로 이루어지는 메소드
        - `Event` 클래스의 `title` 필드 `not null` 검사
          ```java
          @Override
          public void validate(Object target, Errors errors) {
              ValidationUtils.rejectIfEmptyOrWhitespace(errors, "title", "Empty", "타이틀이 비어있으면 안됩니다.");
          }
          ```
2. 어노테이션 사용
    1. `LocalValidatorFactoryBean` 클래스를 의존성 주입을 받아 사용
        ```java
        @Autowired
        private Validator validator;
        ```
    2. 유효성 검증을 하고자 하는 필드에 어노테이션을 사용한다.
        ```java
        public class Event {
          private Long idx;

          @NotEmpty
          private String title;
        }
        ```


</details>
