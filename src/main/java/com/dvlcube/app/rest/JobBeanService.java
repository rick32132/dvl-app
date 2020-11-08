package com.dvlcube.app.rest;

import static com.dvlcube.app.manager.data.e.Menu.CONFIGURATION;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import com.dvlcube.app.jpa.repo.JobBeanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.dvlcube.app.interfaces.MenuItem;
import com.dvlcube.app.manager.data.JobBean;
import com.dvlcube.app.manager.data.vo.MxRestResponse;
import com.dvlcube.utils.interfaces.rest.MxFilterableBeanService;


@RestController
@MenuItem(value = CONFIGURATION)
@RequestMapping("${dvl.rest.prefix}/jobs")
public class JobBeanService implements MxFilterableBeanService<JobBean, Long> {

    @Autowired
    private JobBeanRepository repo;

    @Override
    @GetMapping
    public Iterable<JobBean> get(@RequestParam Map<String, String> params) {
        return repo.firstPage();
    }

    @Override
    @GetMapping("/{id}")
    public Optional<JobBean> get(@PathVariable Long id) {
        return repo.findById(id);
    }

    @Override
    @PostMapping
    public MxRestResponse post(@Valid @RequestBody JobBean body) {
        JobBean save = repo.save(body);
        return GenericRestResponse.ok(save.getId());
    }

    @GetMapping("/like")
    public Iterable<JobBean> getLike(@RequestParam(required = true) String id) {
        return repo.findByNameContaining(id);
    }

    @PutMapping("/{id}")
    public MxRestResponse put(@PathVariable Long id, @RequestBody JobBean body){
        repo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("JobBean")
        );
        body.setId(id);
        repo.save(body);
        return GenericRestResponse.ok(body);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        repo.deleteById(id);
    }
}
