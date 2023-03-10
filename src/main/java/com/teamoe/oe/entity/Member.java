package com.teamoe.oe.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

@NoArgsConstructor
@DynamicUpdate
@Entity
@Getter
@Table(name = "member")
public class Member {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "member_id")
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "email", nullable = false)
  private String email;

  @Column(name = "provider", nullable = false)
  private String provider;

  @Column(name = "nickname", nullable = true, unique = true)
  private String nickname;

  @Builder
  public Member(Long id, String name, String email, String provider, String nickname) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.provider = provider;
    this.nickname = nickname;
  }

  public Member update(String name, String email) {
    this.name = name;
    this.email = email;
    return this;
  }
}
