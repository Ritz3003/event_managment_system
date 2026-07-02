package com.ritu.eventplatform.specification;

import com.ritu.eventplatform.entity.Event;
import com.ritu.eventplatform.enums.EventStatus;
import org.springframework.data.jpa.domain.Specification;

public class EventSpecification {

    public static Specification<Event> isNotDeleted() {
        return (root, query, cb) ->
                cb.equal(root.get("deleted"), false);
    }

    public static Specification<Event> hasStatus(EventStatus status) {
        return (root, query, cb) ->
                status == null ? null :
                        cb.equal(root.get("status"), status);
    }

    public static Specification<Event> hasLocation(String location) {
        return (root, query, cb) ->
                location == null ? null :
                        cb.like(root.get("location"),
                                "%" + location + "%");
    }

    public static Specification<Event> searchTitle(String keyword) {
        return (root, query, cb) ->
                keyword == null ? null :
                        cb.like(root.get("title"),
                                "%" + keyword + "%");
    }
}