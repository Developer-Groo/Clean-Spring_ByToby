package tobyspring.splearn;

import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.*;

class SplearnApplicationTest {

    @Test
    void run() {
        try(MockedStatic<SpringApplication> mocked = mockStatic(SpringApplication.class)) {
            SplearnApplication.main(new String[0]);

            mocked.verify(() -> SpringApplication.run(SplearnApplication.class, new String[0]));
        }
    }
}
