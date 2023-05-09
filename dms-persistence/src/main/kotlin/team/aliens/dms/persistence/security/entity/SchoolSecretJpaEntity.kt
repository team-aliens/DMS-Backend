package team.aliens.dms.persistence.security.entity

import team.aliens.dms.common.annotation.EncryptType
import team.aliens.dms.common.annotation.EncryptedColumn
import team.aliens.dms.persistence.school.entity.SchoolJpaEntity
import java.util.UUID
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.MapsId
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "tbl_school_secret")
class SchoolSecretJpaEntity(

    @Id
    val schoolId: UUID,

    @MapsId
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", columnDefinition = "BINARY(16)", nullable = false)
    val school: SchoolJpaEntity?,

    @EncryptedColumn(type = EncryptType.ASYMMETRIC)
    @Column(columnDefinition = "TEXT", nullable = false)
    val secretKey: String

)
