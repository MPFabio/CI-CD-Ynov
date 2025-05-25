package io.github.pause_clope.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "user_data")
public class UserData {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    @Column
    private Long clicks;

    public UserData(String nickname, Long clicks) {
        this.nickname = nickname;
        this.clicks = clicks;
    }

    public UserData() {

    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Long getClicks() {
        return clicks;
    }

    public void setClicks(Long clicks) {
        this.clicks = clicks;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
