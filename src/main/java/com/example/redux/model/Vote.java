package com.example.redux.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "votes")
public class Vote {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voteId;
    private VoteType voteType;
    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "postId" , referencedColumnName = "postId")
    private Post post;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId" , referencedColumnName = "userId")
    private  User user;
}
