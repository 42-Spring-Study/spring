package me.staek.shop.domain;

import jakarta.persistence.*;
import me.staek.shop.domain.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Or;

import java.time.LocalDateTime;
import java.util.List;


/**
 * jpql select test
 */
public class _06_JPQL {

    @BeforeAll
    public static void createData() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();

            createTeam(em);
            createMember(em);
            registTeam(em);

            createBook(em);
            createMovie(em);

            createOrder(em);

            em.flush();
            em.clear();

            Member member = em.createQuery("select m from Member m where m.name = :name", Member.class)
                    .setParameter("name", "seongtki1").getSingleResult();

            System.out.println(member);
            tx.commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }

    /**
     * 특정 멤버가 주문을 한다.
     * Item 들을 고르고 Order_Item 테이블에 관계를 등록한다.
     * 이 때 주문에 대한 Delivery에 상태, 주소를 등록한다.
     * @param em
     */
    private static void createOrder(EntityManager em) {

        Member member = em.createQuery("select m from Member m where m.name='seongtki1'", Member.class).getSingleResult();
        Order order = new Order();
        order.setMember(member);
        order.setOrderStatus(OrderStatus.ORDERED);

        Book book = em.createQuery("select m from Book m where m.name='JPA5'", Book.class).getSingleResult();
        Movie movie = em.createQuery("select m from Movie m where m.actor='maria4'", Movie.class).getSingleResult();

        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setOrderPrice(2000);
        orderItem.setCount(10);
        orderItem.addOrderItem(order, book);


        OrderItem orderItem2 = new OrderItem();
        orderItem2.setOrder(order);
        orderItem2.setOrderPrice(200000);
        orderItem2.setCount(100);
        orderItem2.addOrderItem(order, movie);

        Delivery delivery = new Delivery();
        delivery.setDeliveryStatus(DeliveryStatus.STARTED);
        System.out.println(member.getAddress());

//        em.createQuery("select a from EntityAddress a " +
//                          "where a.id = (select max(m.addressHistory.size)+2 from Member m join fetch m.addressHistory " +
//                            "                     where m.name='seongtki1')" ,EntityAddress.class).getSingleResult();
//        em.createQuery("select m" +
//                " from EntityAddress m join fetch  " +
//                " where 1=1 " +
//                "  and m.name='seongtki1'" +
//                "  and m.add = (select max(b.adress_id) from address b where b.member_id = a.member_id)",EntityAddress.class).getSingleResult();


        EntityAddress entityAddress = (EntityAddress) em.createNativeQuery("select a.*\n" +
                "from member m, address a \n" +
                "where m.member_id = a.member_id\n" +
                "    and m.name='seongtki1'\n" +
                "    and a.adress_id = (select max(b.adress_id) from address b where b.member_id = a.member_id)", EntityAddress.class).getSingleResult();
        delivery.setAddress(entityAddress.getAddress());
        order.addDelivery(delivery);

        em.persist(delivery);
        em.persist(order);

        em.persist(orderItem);
        em.persist(orderItem2);

    }

    private static void createTeam(EntityManager em) {
        for (int i=0 ;i<3 ; i++) {
            Team team = new Team();
            team.setName("team" + i);
            em.persist(team);
        }
    }

    private static void registTeam(EntityManager em) {
        List<Member> list = em.createQuery("select m from Member m where m.name in ('seongtki1', 'seongtki2', 'seongtki3')", Member.class).getResultList();
        Team team = em.createQuery("select m from Team m where m.name='team0'", Team.class).getSingleResult();
        for (Member m : list)
            team.addMember(m);
        em.persist(team);

        list = em.createQuery("select m from Member m where m.name in ('seongtki4', 'seongtki5', 'seongtki6')", Member.class).getResultList();
        team = em.createQuery("select m from Team m where m.name='team1'", Team.class).getSingleResult();
        for (Member m : list)
            team.addMember(m);
        em.persist(team);

        list = em.createQuery("select m from Member m where m.name in ('seongtki7', 'seongtki8', 'seongtki9')", Member.class).getResultList();
        team = em.createQuery("select m from Team m where m.name='team2'", Team.class).getSingleResult();
        for (Member m : list)
            team.addMember(m);
        em.persist(team);
    }

    private static void createMember(EntityManager em) {
        for (int i=0 ; i<30 ; i++) {
            Member member = new Member();
            member.setName("seongtki" + i);
//        member.setTeam(team);
            member.setPeriod(new Period(LocalDateTime.now(), LocalDateTime.now()));

            member.getFavoliteFoods().add("치킨");
            member.getFavoliteFoods().add("피자");
            member.getFavoliteFoods().add("간장게장");

            Address address = new Address("city1", "zipcode1", "street1");
            Address address2 = new Address("city2", "zipcode2", "street2");
            member.getAddressHistory().add(new EntityAddress(address));
            member.getAddressHistory().add(new EntityAddress(address2));

            em.persist(member);
        }
    }

    private static void createBook(EntityManager em) {
        for (int i=0 ;i<10 ; i++) {
            Book book = new Book();
            book.setAuthor("김영한" + i);
            book.setIsbn("123123"+ i);
            book.setStockQuantity(2);
            book.setPrice(20000);
            book.setName("JPA"+ i);
            em.persist(book);
        }
    }

    private static void createMovie(EntityManager em) {
        for (int i=0 ;i<10 ; i++) {
            Movie movie = new Movie();
            movie.setName("Only If" + i);
            movie.setPrice(10000);
            movie.setStockQuantity(1);
            movie.setActor("maria" + i);
            movie.setDirector("steve" + i);
            em.persist(movie);
        }
    }


    @AfterAll
    public static void deleteAll() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {

            tx.begin();
//            em.createQuery("delete from EntityAddress ").executeUpdate();
//            em.createQuery("delete from OrderItem ").executeUpdate();
//            em.createQuery("delete from Item").executeUpdate();
//            em.createQuery("delete from Order ").executeUpdate();
//            em.createQuery("delete from Delivery ").executeUpdate();
//            em.createQuery("delete from Member").executeUpdate();
//            em.createQuery("delete from Team").executeUpdate();
            tx.commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
    EntityManager em = emf.createEntityManager();

    /**
     * 파라미터 바인딩 (이름기준)
     */
    @Test
    public void 파라미터_바인딩() {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            Member member = em.createQuery("select m from Member m where m.name = :name", Member.class)
                    .setParameter("name", "seongtki1").getSingleResult();

            System.out.println(member);
            tx.commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }

    /**
     * 프로젝션
     * 1) Entity
     * 2) Enbedded Type 프로젝션
     * 3) 스칼라 타입 프로젝션
     */
    @Test
    public void 프로젝션() {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();


            /**
             * Entity 프로젝션
             */
            em.createQuery("select m from Member m", Member.class).getResultList();

            /**
             * Entity 프로젝션 > 묵시적조인
             * 예측이 어려워 지양
             */
//            List<Team> teamList = em.createQuery("select m.team from Member m", Team.class).getResultList();

            /**
             * Entity 프로젝션 > 명시적조인
             */
            List<Team> teamList = em.createQuery("select t from Member m join m.team t", Team.class).getResultList();
            System.out.println(teamList.size());

            /**
             * Enbedded Type 프로젝션
             */
            List<Address> resultList = em.createQuery("select d.address from Delivery d", Address.class).getResultList();
            System.out.println(resultList.size());

            /**
             * 스칼라 타입 프로젝션 > Query 타입 조회
             */
            List list0 = em.createQuery("select m.id, m.name from Member m").getResultList();
            Object[] o = (Object[]) list0.get(0);
            System.out.println("id: " + o[0]);
            System.out.println("name: " + o[1]);

            /**
             * 스칼라 타입 프로젝션 > Object[] 타입 조회
             */
            @SuppressWarnings("unchecked")
            List<Object[]> list = em.createQuery("select m.id, m.name from Member m").getResultList();
            Object[] objects = list.get(0);
            System.out.println("id: " + objects[0]);
            System.out.println("name: " + objects[1]);

            /**
             * String Type 이라서 풀패키지를 모두 적어주어야 함
             * > query dsl 로 개선 가능
             */
            List<MemberDto> resultList1 = em.createQuery("select new me.staek.shop.domain.MemberDto(m.id, m.name) from Member m", MemberDto.class).getResultList();
            MemberDto memberDto = resultList1.get(0);
            System.out.println(memberDto.getId() + " " + memberDto.getName());
            tx.commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }


    /**
     * oracle 페이징: rownum 사용을 왜 안하는 지 모르겠음..
     *
     * < 현재 이렇게 나온다 >
     * select
     *             *
     *         from
     *             (select
     *                 m1_0.MEMBER_ID c0,
     *                 m1_0.name c9,
     *                 row_number() over(
     *             order by
     *                 m1_0.name desc) rn
     *             from
     *                 Member m1_0) r_0_
     *         where
     *             r_0_.rn<=13
     *             and r_0_.rn>3
     *         order by
     *             r_0_.rn
     * ;;
     *
     * <이렇게 나와야 할 거 같은데..>
     * SELECT * FROM
     *  ( SELECT ROW_.*, ROWNUM ROWNUM_
     *  FROM
     *  ( SELECT
     *  M.member_ID AS ID,
     *  M.TEAM_ID AS TEAM_ID,
     *  M.NAME AS NAME
     *  FROM MEMBER M
     *  ORDER BY M.NAME desc
     *  ) ROW_
     *  WHERE ROWNUM <= 13
     *  )
     * WHERE ROWNUM_ > 3
     */
    @Test
    @DisplayName("페이징")
    public void test3() {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            List<Member> list = em.createQuery("select m from Member m order by m.name desc", Member.class)
                    .setFirstResult(3)
                    .setMaxResults(10)
                    .getResultList();
            for (Member m : list){
                System.out.println(m.getName() + " " + m.getId());
            }
            tx.commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }


    @Test
    @DisplayName("inner join")
    public void test4() {
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            /**
             * team : 지연로딩
             */
            List<Member> list = em.createQuery("select m from Member m inner join m.team t", Member.class)
                    .getResultList();
            for (Member m : list){
                System.out.println(m.getName());
//                System.out.println(m.getTeam().getName() + " " + m.getName());
            }
            tx.commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }

    @Test
    @DisplayName("left outer join")
    public void test5() {
        try {
            /**
             * team : 지연로딩
             */
            List<Member> list = em.createQuery("select m from Member m left outer join m.team t", Member.class)
                    .getResultList();
            for (Member m : list){
                System.out.println(m.getName());

//                System.out.println(m.getTeam().getName() + " " + m.getName());
            }
        } catch (PersistenceException e) {
        } finally {
        }
    }


    @Test
    @DisplayName("cross join")
    public void test6() {
        try {
            /**
             * team : 지연로딩
             */
            List<Member> list = em.createQuery("select m from Member m cross join Team t where m.id = t.id", Member.class)
                    .getResultList();
            for (Member m : list){
                System.out.println(m.getName());

//                System.out.println(m.getTeam().getName() + " " + m.getName());
            }
        } catch (PersistenceException e) {
        } finally {
        }
    }


    @Test
    @DisplayName("left join on 조건 (연관관계 존재)")
    public void test7() {
        try {
            /**
             * team : 지연로딩
             */
            List<Member> list = em.createQuery("select m from Member m left join m.team t on t.name = 'teamA'", Member.class)
                    .getResultList();
            for (Member m : list){
                System.out.println(m.getName());

//                System.out.println(m.getTeam().getName() + " " + m.getName());
            }
        } catch (PersistenceException e) {
        } finally {
        }
    }

    @Test
    @DisplayName("left join on 조건 (연관관계 미존재)")
    public void test8() {
        try {
            /**
             * team : 지연로딩
             */
            List<Member> list = em.createQuery("select m from Member m left join Team t on t.name = 'teamA'", Member.class)
                    .getResultList();
            for (Member m : list){
                System.out.println(m.getName());

//                System.out.println(m.getTeam().getName() + " " + m.getName());
            }
        } catch (PersistenceException e) {
        } finally {
        }
    }


    @Test
    @DisplayName("서브쿼리 - from view")
    public void test9() {
        try {
            /**
             * select *
             * from
             * (select * from member
             * where team_id is not null) m left join team t
             * on m.team_id = t.team_id
             *
             *
             * from view의 내용을 as 로 작성해야 외부에서 사용할 수 있는거 같다.
             * https://in.relation.to/2022/06/24/hibernate-orm-61-features/
             */
            List<MemberDto> list = em.createQuery("select new me.staek.shop.domain.MemberDto(m.id, m.name) " +
                            "from (select mm.id as id, mm.team.id as team_id, mm.name as name from Member mm where mm.team.id is not null) as m " +
                            "left join Team t " +
                            "on m.team_id = t.id", MemberDto.class)
                    .getResultList();
            for (MemberDto m : list)
                System.out.println(m.getId() + " " + m.getName());
        } catch (PersistenceException e) {}
    }


    @Test
    @DisplayName("서브쿼리 where count - 한 건이라도 주문한 고객")
    public void test10() {
        try {
            /**
             * select * from Member m
             * where (select count(1) from Orders o where m.member_id = o.member_id) > 0;
             */
            List<Member> list
                    = em.createQuery("select m from Member m " +
                                        "where (select count(m.id) from Order o where m.id = o.member.id) > 0", Member.class)
                    .getResultList();
            for (Member m : list)
                System.out.println(m.getId() + " " + m.getName());
        } catch (PersistenceException e) {}
    }


    @Test
    @DisplayName("서브쿼리 exists - team1 소속인 회원")
    public void test11() {
        try {
            /**
             * select * from Member m
             * where exists (select * from team t where m.team_id = t.team_id and t.name = 'team1');
             */
            List<Member> list
                    = em.createQuery("select m from Member m " +
                            "where exists (select t from m.team t where t.name = 'team1')", Member.class)
                    .getResultList();
            for (Member m : list)
                System.out.println(m.getId() + " " + m.getName());
        } catch (PersistenceException e) {}
    }

    @Test
    @DisplayName("서브쿼리 where - 나이가 평균보다 많은 회원 ------- 아직안함")
    public void test12() {
        try {
            /**
             * select m from Member m
             * where m.age > (select avg(m2.age) from Member m2)
             */
//            List<Member> list
//                    = em.createQuery("select m from Member m " +
//                            "where m.team = ANY (select t from Team t)", Member.class)
//                    .getResultList();
//            for (Member m : list)
//                System.out.println(m.getId() + " " + m.getName());
        } catch (PersistenceException e) {}
    }



    @Test
    @DisplayName("서브쿼리 ALL - 전체 상품 각각의 재고보다 주문량이 많은 주문들 ------- 아직안함")
    public void test13() {
        try {
            /**
             * select * from Order o
             * where o.orderAmount > ALL (select p.stockAmount from Product p);
             */
//            List<Member> list
//                    = em.createQuery("select m from Member m " +
//                            "where m.team = ANY (select t from Team t)", Member.class)
//                    .getResultList();
//            for (Member m : list)
//                System.out.println(m.getId() + " " + m.getName());
        } catch (PersistenceException e) {}
    }


    @Test
    @DisplayName("서브쿼리 ANY - 어떤 팀이든 소속된 member")
    public void test14() {
        try {
            /**
             * select * from Member m
             * where m.team_id = ANY (select t.team_id from Team t);
             *
             * 아래 쿼리에서 team 자체를 비교하는데, 이는 내부적으로 식별자비교를 암시한다. (team_id)
             */
            List<Member> list
                    = em.createQuery("select m from Member m " +
                            "where m.team = ANY (select t from Team t)", Member.class)
                    .getResultList();
            for (Member m : list)
                System.out.println(m.getId() + " " + m.getName());
        } catch (PersistenceException e) {}
    }

    @Test
    @DisplayName("서브쿼리 스칼라 - 연관없는 쿼리")
    public void test15() {
        try {
            /**
             *select (select count(*) from team) as t from member m ;
             */
            List<Long> resultList = em.createQuery("select (select count(*) from Team t) from Member m", Long.class)
                    .getResultList();
            System.out.println(resultList.size());
            for (Long m : resultList)
                System.out.println(m);
        } catch (PersistenceException e) {}
    }


    @Test
    @DisplayName("슈퍼서브타입 다형성 조회")
    public void test16() {
        try {
            /**
             * select * from item
             * where dtype = 'Book';
             */
            List<Item> resultList = em.createQuery("select i from Item i where type(i) = Book", Item.class)
                    .getResultList();
            for (Item m : resultList)
                System.out.println(m.getName() + " " + m.getPrice());
        } catch (PersistenceException e) {}
    }


    @Test
    @DisplayName("ENUM Type 조회")
    public void test17() {
        try {
            /**
             * select * from orders
             * where orderStatus='ORDERED';
             */
            List<Order> resultList
                    = em.createQuery(
                            "select o from Order o where o.orderStatus = me.staek.shop.domain.OrderStatus.ORDERED"
                            , Order.class)
                        .getResultList();
            for (Order m : resultList)
                System.out.println(m.getOrderStatus());
        } catch (PersistenceException e) {}
    }
}
