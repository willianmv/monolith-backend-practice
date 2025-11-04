package com.simple.blog.backend.infra.config.initializer;

import com.simple.blog.backend.core.domain.Profile;
import com.simple.blog.backend.core.domain.RoleType;
import com.simple.blog.backend.core.domain.Tag;
import com.simple.blog.backend.core.domain.TagType;
import com.simple.blog.backend.core.gateway.repository.IProfileRepository;
import com.simple.blog.backend.core.gateway.repository.ITagRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class DataInitializer {

    private static final Logger log = LoggerFactory.getLogger(DataInitializer.class);
    private final IProfileRepository profileRepositoryGateway;
    private final ITagRepository tagRepositoryGateway;

    public DataInitializer(IProfileRepository profileRepositoryGateway, ITagRepository tagRepositoryGateway) {
        this.profileRepositoryGateway = profileRepositoryGateway;
        this.tagRepositoryGateway = tagRepositoryGateway;
    }

    public void initializeData(){
        initializeProfiles();
        initializeTags();
    }

    private void initializeProfiles(){
        log.info("🔍 Initializing default profiles...");
        Arrays.stream(RoleType.values()).forEach(role -> {
            try{
                boolean exists = profileRepositoryGateway.existsByRole(role);
                if(!exists){
                    Profile profile = new Profile();
                    profile.setRole(role);
                    profile.setDescription(role.getDescription());
                    profileRepositoryGateway.save(profile);
                    log.info("✅ Profile created: {}", role.name());

                } else {
                    log.info("ℹ️ Profile already exists: {}", role.name());
                }

            } catch (Exception e) {
                log.error("❌ Error creating profile {}: {}", role.name(), e.getMessage(), e);
            }

        });
        log.info("✅ Profile initialization concluded.");
    }

    private void initializeTags(){
        log.info("🔍 Initializing default tags...");
        Arrays.stream(TagType.values()).forEach( type -> {
            try{
                boolean exists = tagRepositoryGateway.existsByTag(type);
                if(!exists){
                    Tag tag = new Tag();
                    tag.setTag(type);
                    tagRepositoryGateway.save(tag);
                    log.info("✅ Tag created: {}", type.name());

                } else{
                    log.info("ℹ️ Tag already exists: {}", type.name());
                }

            } catch (Exception e) {
                log.error("❌ Error creating tag {}: {}", type.name(), e.getMessage(), e);
            }
        });
        log.info("✅ Tag initialization concluded.");
    }
}
