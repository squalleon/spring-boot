package com.jojoldu.book.springboot.web.dto;

import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class HelloResponseDtoTest {

    @Test
    public void 롬복_기능_테스트() {
        //given
        String name = "test";
        int amount = 1000;

        //when
        HelloResponseDto dto = new HelloResponseDto(name, amount);

        //then
        assertThat(dto.getName()).isEqualTo(name);
        assertThat(dto.getAmount()).isEqualTo(amount);
/**
 * JUnit의 기본 assertThat이 아닌 assertj의 assertThat 사용
 * assertj의 장점에 대한 자세한 설명은 백기선님의 유튜브 'assertJ가 JUnit의 assertThat 보다 편리한 이유'를
 * 참고하면 좋습니다.(http://bit.ly/30vm9Lg)
 * 1) assertThat
 *   - assertj라는 테스트 검증 라이브러리의 검증 메소드입니다.
 *   - 검증하고 싶은 대상을 메소드 인자로 받습니다.
 *   - 메소드 체이닝이 지원되어 isEqualTo와 같이 메소드를 이어서 사용할 수 있습니다.
 * 2) isEqualTo
 *   - assertj의 동등 비교 메소드입니다.
 *   - assertThat에 있는 값과 isEqualTo의 값을 비교해서 같을 때만 성공입니다.
 */
    }
}
