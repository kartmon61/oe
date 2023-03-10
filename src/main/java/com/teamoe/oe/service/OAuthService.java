package com.teamoe.oe.service;

import com.teamoe.oe.dto.MemberProfile;
import com.teamoe.oe.entity.Member;
import com.teamoe.oe.repository.MemberRepository;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OAuthService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

  private final MemberRepository memberRepository;

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
    OAuth2UserService delegate = new DefaultOAuth2UserService();
    OAuth2User oAuth2User = delegate.loadUser(userRequest);

    String registrationId = userRequest.getClientRegistration()
        .getRegistrationId();
    String userNameAttributeName = userRequest.getClientRegistration()
        .getProviderDetails()
        .getUserInfoEndpoint()
        .getUserNameAttributeName();

    Map<String, Object> attributes = oAuth2User.getAttributes();
    MemberProfile memberProfile = OAuthAttributes.extract(registrationId, attributes);
    memberProfile.setProvider(registrationId);
    saveOrUpdate(memberProfile);
    Map<String, Object> customAttribute = customAttribute(attributes, userNameAttributeName,
        memberProfile, registrationId);

    return new DefaultOAuth2User(
        Collections.singleton(new SimpleGrantedAuthority("USER")),
        customAttribute,
        userNameAttributeName
    );
  }

  private Map customAttribute(Map attributes, String userNameAttributeName,
      MemberProfile memberProfile, String registrationId) {
    LinkedHashMap<String, Object> customAttribute = new LinkedHashMap<>();
    customAttribute.put(userNameAttributeName, attributes.get(userNameAttributeName));
    customAttribute.put("provider", registrationId);
    customAttribute.put("name", memberProfile.getName());
    customAttribute.put("email", memberProfile.getEmail());
    return customAttribute;
  }

  private Member saveOrUpdate(MemberProfile memberProfile) {
    Member member = memberRepository.findByEmailAndProvider(
        memberProfile.getEmail(),
        memberProfile.getProvider())
      .map(data -> data.update(memberProfile.getName(), memberProfile.getEmail()))
      .orElse(memberProfile.toMember());

    return memberRepository.save(member);
  }
}
