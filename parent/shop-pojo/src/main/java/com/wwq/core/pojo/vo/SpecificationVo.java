package com.wwq.core.pojo.vo;

import com.wwq.core.pojo.specification.Specification;
import com.wwq.core.pojo.specification.SpecificationOption;

import java.io.Serializable;
import java.util.List;

public class SpecificationVo implements Serializable {

    //规格对象
    private Specification specification;
    //规格选项列表
    private List<SpecificationOption> specificationOptionList;

    public Specification getSpecification() {
        return specification;
    }

    public void setSpecification(Specification specification) {
        this.specification = specification;
    }

    public List<SpecificationOption> getSpecificationOptionList() {
        return specificationOptionList;
    }

    public void setSpecificationOptionList(List<SpecificationOption> specificationOptionList) {
        this.specificationOptionList = specificationOptionList;
    }
}
