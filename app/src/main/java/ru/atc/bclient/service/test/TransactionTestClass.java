package ru.atc.bclient.service.test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.atc.bclient.model.entity.User;
import ru.atc.bclient.model.repository.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;


@Service
// this class was created only in order to test transactions
public class TransactionTestClass {
    @Autowired
    UserRepository userRepository;
    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Transactional(propagation = Propagation.MANDATORY, isolation = Isolation.SERIALIZABLE)
    public void saveUser() throws InterruptedException{
        System.out.println("tst");
//        EntityManager entityManager = entityManagerFactory.createEntityManager();
//        entityManager.getTransaction().begin();
//        entityManager.createNativeQuery("LOCK TABLE postgres.bclient3.dim_user IN EXCLUSIVE MODE").executeUpdate();
//
//        User user = new User();
//        // находит максимальный id в таблице и увеличивает его значение на 1
//        Integer id = userRepository.findMaxId() + 1;
//        user.setId(id);
//        user.setLogin("tst" + id);
//        user.setFullName("tst");
//        user.setPassword("tst");
//
//        Thread.sleep(30000);
//
//        userRepository.save(user);
//        entityManager.getTransaction().commit();
//        entityManager.close();
    }
}
