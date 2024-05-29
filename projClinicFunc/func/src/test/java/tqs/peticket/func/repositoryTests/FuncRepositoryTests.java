package tqs.peticket.func.repositoryTests;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import tqs.peticket.func.model.Func;
import tqs.peticket.func.repository.FuncRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(properties = {"spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.H2Dialect"})
public class FuncRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private FuncRepository funcRepository;

    @Test
    void whenFindById_thenReturnFunc() {
        UUID UserId = UUID.randomUUID();
        Func func = new Func(UserId);
        entityManager.persistAndFlush(func);
        Func found = funcRepository.findById(func.getId());
        assertThat(found).isEqualTo(func);
    }

    @Test
    void whenInvalidFuncId_thenReturnNull() {
        UUID uuid = UUID.randomUUID();
        Func fromDb = funcRepository.findById(uuid);
        assertThat(fromDb).isNull();
    }

    @Test
    void whenSaveFunc_thenReturnSavedFunc() {
        UUID uuid = UUID.randomUUID();
        Func func = new Func(uuid);
        Func saved = funcRepository.save(func);
        assertThat(saved).isEqualTo(func);
    }

    @Test
    void whenDeleteById_thenIsDeleted() {
        UUID usrid = UUID.randomUUID();
        Func func = new Func(usrid);
        entityManager.persistAndFlush(func);
        funcRepository.deleteById(func.getId());
        Func fromDb = funcRepository.findById(func.getId());
        assertThat(fromDb).isNull();
    }

    @Test
    void whenExistsById_thenReturnTrue() {
        UUID usrid = UUID.randomUUID();
        Func func = new Func(usrid);
        entityManager.persistAndFlush(func);
        assertThat(funcRepository.existsById(func.getId())).isTrue();
    }

    @Test
    void whenExistsByUserId_thenReturnTrue() {
        Func func = new Func();
        UUID userId = UUID.randomUUID();
        func.setUserId(userId);
        entityManager.persistAndFlush(func);
        assertThat(funcRepository.existsByUserId(userId)).isTrue();
    }

    @Test
    void whenFindByUserId_thenReturnFunc() {
        Func func = new Func();
        UUID userId = UUID.randomUUID();
        func.setUserId(userId);
        entityManager.persistAndFlush(func);
        Func found = funcRepository.findByUserId(userId);
        assertThat(found).isEqualTo(func);
    }
}
