package uk.gov.digital.ho.hocs.info.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ListIndexBase;

import java.io.Serializable;
import java.util.List;

@jakarta.persistence.Entity
@Table(name = "workstack_type")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
public class WorkstackType implements Serializable {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_system_name", length = 25)
    private String parentSystemName;

    @Column(name = "type", length = 50)
    private String type;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "workstack_column_type",
               joinColumns = @JoinColumn(name = "workstack_type_type", referencedColumnName = "type"),
               inverseJoinColumns = @JoinColumn(name = "workstack_column_uuid", referencedColumnName = "uuid"))
    @OrderColumn(name = "workstack_column_order")
    @ListIndexBase(value = 1)
    private List<WorkstackColumn> workstackColumns;

}
