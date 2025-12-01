package aiagents.bazar.data.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "claim", indexes = {
    @Index(name = "idx_claim_status", columnList = "status"),
    @Index(name = "idx_claim_task_id", columnList = "task_id"),
    @Index(name = "idx_claim_telegram_user_id", columnList = "telegram_user_id"),
    @Index(name = "idx_claim_created_at", columnList = "created_at"),
    @Index(name = "idx_claim_task_status", columnList = "task_id, status")
})
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50, nullable = false)
    private ClaimStatus status;

    @Column(name = "message", columnDefinition = "TEXT")
    private String message;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "telegram_user_id", nullable = false)
    private TelegramUser telegramUser;
}
