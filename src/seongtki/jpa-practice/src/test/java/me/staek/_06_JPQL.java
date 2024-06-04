package me.staek;

import jakarta.persistence.*;
import me.staek.shop.domain.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

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
        for (int i=0 ; i<10 ; i++) {
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
//        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
//        EntityManager em = emf.createEntityManager();
//        EntityTransaction tx = em.getTransaction();
//        try {
//            tx.begin();
//            em.createQuery("delete from Member").executeUpdate();
//            tx.commit();
//        } catch (PersistenceException e) {
//            e.printStackTrace();
//            tx.rollback();
//        } finally {
//            em.close();
//            emf.close();
//        }
    }

    @Test
    public void test() {
        System.out.println("agaweg");
    }

}
