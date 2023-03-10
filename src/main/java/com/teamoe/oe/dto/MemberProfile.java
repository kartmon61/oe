package com.teamoe.oe.dto;

import com.teamoe.oe.entity.Member;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class MemberProfile {
  private String name;
  private String email;
  @Setter
  private String provider;
  @Setter
  private String nickname;

  public Member toMember() {
    return Member.builder()
        .name(name)
        .email(email)
        .provider(provider)
        .build();
  }
}
