package kvoting.intern.flowerwebapp.domain;

import kvoting.intern.flowerwebapp.type.DB;
import kvoting.intern.flowerwebapp.type.DataType;
import kvoting.intern.flowerwebapp.word.Word;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;


@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@lombok.Data
public class DomainBase {
    @Column(name = "DOMAIN_NAME")
    private String name;
    @Column(name = "DOMAIN_ENG_NAME")
    private String engName;
    @Column(name = "DOMAIN_DESC_CONT")
    private String description;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "DBTYPE_TPCD")
    private DB db;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "DATATYPE_TPCD")
    private DataType dataType;
    @Column(name = "DOMAIN_SIZE")
    private Integer size;
    @Column(name = "DOMAIN_SCALE")
    private Integer scale;
    @Column(name = "NN_YN")
    private Boolean nullable;
    @ManyToMany(fetch = FetchType.EAGER)
    @OrderColumn
    @JoinTable(name = "CC_DOMAIN_REG_WORD_CC",
            joinColumns = @JoinColumn(name = "DOMAIN_REG_ID"),
            inverseJoinColumns = @JoinColumn(name = "WORD_ID"))
    private List<Word> words;

    @PrePersist
    public void setUp() {
        String name = "";
        String engName = "";
        for (Word word : words) {
            name += word.getName() + "_";
            engName += word.getEngName() + "_";
        }
        this.name = name.substring(0, name.length() - 1);
        this.engName = engName.substring(0, engName.length() - 1);
    }
}
