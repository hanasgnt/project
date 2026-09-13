package com.bootcamp.project.entity;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "transactions")
public class Transactions {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TransactionType type;

    // Dipakai kalau transaksi IN
    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Suppliers supplier;

    // Dipakai kalau transaksi OUT
    @Column(name = "customer_name", length = 255)
    private String customerName;

    @Column(name = "transaction_date", nullable = false)
    private LocalDateTime transactionDate;

    @OneToMany(
        mappedBy = "transaction",
        cascade = CascadeType.ALL,
        orphanRemoval = true
    )
    private List<TransactionDetails> details = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        transactionDate = LocalDateTime.now();
    }

    public Transactions(Long id) {
        this.id = id;
    }
}