package com.dvlcube.app.jpa.repo;

import org.springframework.stereotype.Repository;

import com.dvlcube.app.manager.data.JobBean;
import com.dvlcube.app.jpa.BasicRepository;
import com.dvlcube.app.jpa.DvlRepository;

import java.util.List;


@Repository
public interface JobBeanRepository extends DvlRepository<JobBean, Long>, BasicRepository<JobBean, Long> {
    public List<JobBean> findByNameContaining(String name);
}
