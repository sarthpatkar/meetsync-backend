package com.meetingscheduler.repository;

import com.meetingscheduler.model.Meeting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface MeetingRepository extends JpaRepository<Meeting, Long> {

    // ✅ Find meetings by organizer's username
    List<Meeting> findByOrganizer_Username(String username);

    // ✅ Find meetings by organizer's ID
    List<Meeting> findByOrganizer_Id(Long organizerId);

    // ✅ Check if a meeting overlaps for the same organizer (excluding the current meeting if updating)
    @Query("SELECT CASE WHEN COUNT(m) > 0 THEN true ELSE false END " +
           "FROM Meeting m " +
           "WHERE m.organizer.id = :organizerId " +
           "AND m.id <> :meetingId " +
           "AND (m.startTime < :endTime AND m.endTime > :startTime)")
    boolean existsByOrganizerAndTimeOverlap(Long organizerId,
                                            LocalDateTime startTime,
                                            LocalDateTime endTime,
                                            Long meetingId);
}