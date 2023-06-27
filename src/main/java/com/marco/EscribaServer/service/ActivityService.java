package com.marco.EscribaServer.service;

import com.marco.EscribaServer.Utils.EntityType;
import com.marco.EscribaServer.entity.ActivityEvent;
import com.marco.EscribaServer.exceptions.Activity.NotExistingActivity;
import com.marco.EscribaServer.repository.ActivityEventRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ActivityService {

    @Autowired
    ActivityEventRepository activityEventRepository;

    public List<ActivityEvent> getRecentActivity(int pageSize, int pageNumber, List<String> usernames) {
        int offset = (pageNumber) * pageSize;
        Pageable pageable = PageRequest.of(offset, pageSize);
        List<ActivityEvent> page = activityEventRepository.findRecentByUsernames(usernames, pageable);
        return page;
    }

    public void createFollowEvent(String following, String follower){
        ActivityEvent activityEvent = new ActivityEvent();
        activityEvent.setUsername(follower);
        activityEvent.setEntityId(following);
        activityEvent.setEntityType(EntityType.USER);
        activityEvent.setAction("FOLLOWS");
        activityEvent.setTimestamp(LocalDateTime.now());

        activityEventRepository.save(activityEvent);

    }

    public void createDocumentCreationEvent(String username, Long documentID){
        ActivityEvent activityEvent = new ActivityEvent();
        activityEvent.setUsername(username);
        activityEvent.setEntityId(documentID.toString());
        activityEvent.setEntityType(EntityType.DOCUMENT);
        activityEvent.setAction("PUBLISHED");
        activityEvent.setTimestamp(LocalDateTime.now());

        activityEventRepository.save(activityEvent);

    }

    public void startedReadingEvent(String username, Long documentID){
        ActivityEvent activityEvent = new ActivityEvent();
        activityEvent.setUsername(username);
        activityEvent.setEntityId(documentID.toString());
        activityEvent.setEntityType(EntityType.DOCUMENT);
        activityEvent.setAction("READING");
        activityEvent.setTimestamp(LocalDateTime.now());

        activityEventRepository.save(activityEvent);
    }
    public void createdReviewEvent(String username, Long reviewID){
        ActivityEvent activityEvent = new ActivityEvent();
        activityEvent.setUsername(username);
        activityEvent.setEntityId(reviewID.toString());
        activityEvent.setEntityType(EntityType.REVIEW);
        activityEvent.setAction("REVIEWED");
        activityEvent.setTimestamp(LocalDateTime.now());

        activityEventRepository.save(activityEvent);
    }
    public void replyToReviewEvent(String username, Long replyID){
        ActivityEvent activityEvent = new ActivityEvent();
        activityEvent.setUsername(username);
        activityEvent.setEntityId(replyID.toString());
        activityEvent.setEntityType(EntityType.REPLY);
        activityEvent.setAction("REPLIED");
        activityEvent.setTimestamp(LocalDateTime.now());

        activityEventRepository.save(activityEvent);

    }
    public void deleteActivityEvent(long activityID){
        Optional<ActivityEvent> activityEventOptional = activityEventRepository.findById(activityID);

        if(!activityEventOptional.isPresent()) throw new NotExistingActivity("The activity with id: " + activityID + " does not exist");

        activityEventRepository.deleteById(activityID);
    }

    public boolean checkIfExist(String entityID, EntityType entityType){
        return activityEventRepository.existsByEntityIdAndEntityType(entityID, entityType);
    }

}