package me.staek;


import jakarta.persistence.*;
import me.staek.shop.domain.*;
import org.junit.jupiter.api.Test;

public class _04_MemberValue {



    /**
     * Embedded Type
     * Collection Value Type
     * Collection Value Type => Entity
     * 위 세 가지 타입에 대해 삭제 추가가 어떤 식으로 진행 되는 지 확인해 보자.
     */
    @Test
    void member_flush() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();

            Member member = new Member();
            member.setAddress(new Address("city", "zipcode", "street"));
            member.setPeriod(new Period());

            Member member2 = new Member();
            Address address = new Address("city2", "zipcode2", "street2");
            member2.setAddress(address);

            Member member3 = new Member();
            /**
             * Embadded 객체가 가변이라면,
             * 값을 변경하여 다른 엔티티에 적용할 때
             * 해당 Embadded 객체를 참조하는 모든 Entity 컬럼 값이 변경된다.
             * => 불변객체를 사용해야 한다.
             */
            address.setCity("city333");
            member3.setAddress(address);

            em.persist(member);
            em.persist(member2);
            em.persist(member3);


            /**
             * Embadded 객체 값을 변경하여도
             * 해당 객체를 참조하는 모든 엔티티의 컬럼 값이 변경된다.
             * => 불변객체를 사용해야 한다.
             */
            address.setStreet("streetupdate");

            member.setAddress(new Address("city by member", "zipcode by member", "street by member"));


            Member member4 = new Member();
            member4.setName("collection value test");
            /**
             * Collection Value Type
             */
//            member4.getAddressHistory().add(new Address("city h1", "zipcode h1", "street h1"));
//            member4.getAddressHistory().add(new Address("city h2", "zipcode h2", "street h2"));
//            member4.getAddressHistory().add(new Address("city h3", "zipcode h3", "street h3"));

            /**
             * Collection Value Type => Entity
             */
            member4.getAddressHistory().add(new EntityAddress(new Address("city h1", "zipcode h1", "street h1")));
            EntityAddress entityAddress = new EntityAddress("city h2", "zipcode h2", "street h2");
            member4.getAddressHistory().add(entityAddress);
            member4.getAddressHistory().add(new EntityAddress(new Address("city h3", "zipcode h3", "street h3")));

            /**
             * Collection Value Type
             */
            member4.getFavoliteFoods().add("햄버거");
            member4.getFavoliteFoods().add("치킨");
            member4.getFavoliteFoods().add("피자");

            em.persist(member4);

            em.flush();
            em.clear();


            Member found4 = em.find(Member.class, member4.getId());

            /**
             * Collection Value Type
             * - 기본적으로 지연로딩 이다.
             */
            for (String s : found4.getFavoliteFoods()) {
                System.out.println(s);
            }

//            for (Address address1 : found4.getAddressHistory()) {
//                System.out.println(address1);
//            }
            for (EntityAddress address1 : found4.getAddressHistory()) {
                System.out.println(address1);
            }

            /**
             * Collection Value Type
             * - String 은 통째로 삭제, 추가 되는데.
             * - 한개 삭제, 한개 insert 로 진행된다.
             */
            found4.getFavoliteFoods().remove("피자");
            found4.getFavoliteFoods().add("밥");

            /**
             * Collection Value Type
             * - 값 객체로 되어 있으면, 객체 째로 삭제 후 추가 할경우
             * - 해당 왜래키에 대한 모든 row가 삭제되고 다시 insert 된다.
             */
//            found4.getAddressHistory().remove(new Address("city h2", "zipcode h2", "street h2"));
//            found4.getAddressHistory().add(new Address("city h4", "zipcode h4", "street h4"));

            /**
             * Collection Value Type => Entity
             * - Collection Entity 를 인덱스말고 객체를 키로 삭제하려면 equals가 잘 구현되어 있어야 함.
             */
            System.out.println("size: " + found4.getAddressHistory().size());
            found4.getAddressHistory().remove(entityAddress);
            System.out.println("size: " + found4.getAddressHistory().size());
            found4.getAddressHistory().add(new EntityAddress(new Address("city h4", "zipcode h4", "street h4")));

            tx.commit();
        } catch (PersistenceException e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
            emf.close();
        }
    }


}
