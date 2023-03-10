package com.teamoe.oe.service;

import com.teamoe.oe.dto.MemberProfile;
import java.util.Arrays;
import java.util.Map;
import java.util.function.Function;

public enum OAuthAttributes {
  GOOGLE("google", (attributes) -> {
    return MemberProfile.builder()
        .name((String) attributes.get("name"))
        .email((String) attributes.get("email"))
        .build();
  }),
  NAVER("naver", (attributes) -> {
    Map<String, Object> response = (Map<String, Object>) attributes.get("response");
    System.out.println(response);
    return MemberProfile.builder()
        .name((String) response.get("name"))
        .email((String) response.get("email"))
        .build();
  });

  private final String registrationId;
  private final Function<Map<String, Object>, MemberProfile> of;

  OAuthAttributes(String registrationId, Function<Map<String, Object>, MemberProfile> of) {
    this.registrationId = registrationId;
    this.of = of;
  }

  public static MemberProfile extract(String registrationId, Map<String, Object> attributes) {
    return Arrays.stream(values())
        .filter(provider -> registrationId.equals(provider.registrationId))
        .findFirst()
        .orElseThrow(IllegalArgumentException::new)
        .of.apply(attributes);
  }
}
