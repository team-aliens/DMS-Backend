package team.aliens.dms.persistence.student.mapper

import org.mapstruct.Mapper
import org.mapstruct.Mapping
import team.aliens.dms.domain.student.model.Student
import team.aliens.dms.persistence.GenericMapper
import team.aliens.dms.persistence.student.entity.StudentEntity

@Mapper
interface StudentMapper : GenericMapper<Student, StudentEntity> {

    @Mapping(source = "userEntity.id", target = "studentId")
    @Mapping(source = "roomEntity.id.roomNumber", target = "roomNumber")
    override fun toDomain(e: StudentEntity): Student

    @Mapping(source = "studentId", target = "userEntity.id")
    @Mapping(source = "roomNumber", target = "roomEntity.id.roomNumber")
    override fun toEntity(d: Student): StudentEntity
}