package aiagents.bazar.data.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "task")
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false, length = 255)
    private String title;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "region", length = 100)
    private String region;

    @Column(name = "price_expected", precision = 19, scale = 2)
    private BigDecimal priceExpected;

    @Column(name = "reward_amount", precision = 19, scale = 2)
    private BigDecimal rewardAmount;

    @Column(name = "reward_percentage", precision = 5, scale = 2)
    private BigDecimal rewardPercentage;

    @Enumerated(EnumType.STRING)
    @Column(name = "reward_type", length = 50)
    private RewardType rewardType;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 50)
    private TaskStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "escrow_status", length = 50)
    private EscrowStatus escrowStatus;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    @UpdateTimestamp
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",nullable =false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "telegram_user", nullable= false)
    private TelegramUser telegramUser;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Claim> claims;
}
