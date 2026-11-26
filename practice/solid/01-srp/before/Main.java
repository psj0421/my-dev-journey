import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Main {
    public static void main(String[] args) {
        LocalDate today = LocalDate.of(2026, 11, 26);

        Assignment java = new Assignment("자바 객체지향 리포트", LocalDate.of(2026, 11, 27), "PERSONAL", null, 0);
        Assignment db = new Assignment("DB 설계 팀 프로젝트", LocalDate.of(2026, 12, 3), "TEAM", "4조", 4);

        java.printReminder(today);
        db.printReminder(today);

        java.saveToFile("assignments.csv");
        db.saveToFile("assignments.csv");
    }
}

/**
 * [SRP 연습] 이 Assignment 클래스는 단일 책임 원칙을 여러 군데에서 어기고 있다.
 * 동작은 정상이다. 동작은 그대로 두고 "구조"만 고치는 게 목표!
 *
 * 실행: java Main.java
 */
class Assignment {
    String title;
    LocalDate dueDate;
    String type;        // "PERSONAL"(개인 과제) 또는 "TEAM"(팀 과제)
    String teamName;    // 팀 과제일 때만 사용
    int memberCount;    // 팀 과제일 때만 사용

    Assignment(String title, LocalDate dueDate, String type, String teamName, int memberCount) {
        this.title = title;
        this.dueDate = dueDate;
        this.type = type;
        this.teamName = teamName;
        this.memberCount = memberCount;
    }

    // 마감까지 남은 날짜 계산
    long daysLeft(LocalDate today) {
        return ChronoUnit.DAYS.between(today, dueDate);
    }

    // 예상 작업 시간 계산
    int estimatedHours() {
        if (type.equals("PERSONAL")) {
            return 5;
        } else if (type.equals("TEAM")) {
            return 12 / memberCount + 2; // 팀원끼리 나누고, 회의 시간 2시간 추가
        }
        return 0;
    }

    // 파일에 저장
    void saveToFile(String fileName) {
        try (FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(title + "," + dueDate + "," + type + "," + teamName + "\n");
        } catch (IOException e) {
            System.out.println("저장 실패: " + e.getMessage());
        }
    }

    // 콘솔에 알림 출력
    void printReminder(LocalDate today) {
        long days = daysLeft(today);
        String mark = days <= 1 ? "🚨" : "📌";
        System.out.println(mark + " [" + title + "] 마감까지 " + days + "일 남음 (예상 " + estimatedHours() + "시간)");
        if (type.equals("TEAM")) {
            System.out.println("   └ 팀: " + teamName + " / " + memberCount + "명");
        }
    }
}
