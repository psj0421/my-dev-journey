import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Main {
    public static void main(String[] args) {
        LocalDate today = LocalDate.of(2026, 11, 26);

        Assignment java = new PersonalAssignment("자바 객체지향 리포트", LocalDate.of(2026, 11, 27));
        Assignment db = new TeamAssignment("DB 설계 팀 프로젝트", LocalDate.of(2026, 12, 3), "4조", 4);

        AssignmentRepository repository=new AssignmentRepository();
        AssignmentNotifier notifier=new AssignmentNotifier();

        notifier.printReminder(java,today);
        notifier.printReminder(db,today);

        repository.saveToFile(java,"assignments.csv");
        repository.saveToFile(db,"assignments.csv");
    }
}

/**
 * [SRP 연습] 이 Assignment 클래스는 단일 책임 원칙을 여러 군데에서 어기고 있다.
 * 동작은 정상이다. 동작은 그대로 두고 "구조"만 고치는 게 목표!
 *
 * 실행: java Main.java
 */
abstract class Assignment {
    String title;
    LocalDate dueDate;

    Assignment(String title, LocalDate dueDate) {
        this.title = title;
        this.dueDate = dueDate;

    }

    // 마감까지 남은 날짜 계산
    long daysLeft(LocalDate today) {
        return ChronoUnit.DAYS.between(today, dueDate);
    }

    // 예상 작업 시간 계산
    abstract int estimatedHours();
    abstract String typeName();
    abstract String extraInfo();
    abstract String teamNameForSave();
}
class TeamAssignment extends Assignment{
    String teamName;
    int memberCount;
    TeamAssignment(String title,LocalDate dueDate,String teamName,int memberCount){
        super(title,dueDate);
        this.teamName=teamName;
        this.memberCount=memberCount;
    }
    @Override
    int estimatedHours(){
        return 12/memberCount +2;
    }
    @Override
    String typeName(){
        return "TEAM";
    }
    @Override
    String extraInfo(){
        return "   └ 팀: " + teamName + " / " + memberCount + "명";
    }
    @Override
    String teamNameForSave(){
        return teamName;
    }
}

class PersonalAssignment extends Assignment{
    PersonalAssignment(String title,LocalDate dueDate){
        super(title, dueDate);
    }
    @Override
    int estimatedHours(){
  return 5;
    }
    @Override
        String typeName(){
    return "PERSONAL";
    }
    @Override
    String extraInfo(){
      return  "";
    }
    @Override
    String teamNameForSave(){
        return "";
    }
}


class AssignmentRepository{
            // 파일에 저장
            void saveToFile(Assignment a,String fileName) {
                try (FileWriter writer = new FileWriter(fileName, true)) {
                    writer.write(a.title + "," + a.dueDate + "," + a.typeName() + "," + a.teamNameForSave() + "\n");
                } catch (IOException e) {
                    System.out.println("저장 실패: " + e.getMessage());
                }
            }
        }

class AssignmentNotifier{
    // 콘솔에 알림 출력
    void printReminder(Assignment a,LocalDate today) {
        long days = a.daysLeft(today);
        String mark = days <= 1 ? "🚨" : "📌";
        System.out.println(mark + " [" + a.title + "] 마감까지 " + days + "일 남음 (예상 " + a.estimatedHours() + "시간)");
        if (!a.extraInfo().isEmpty()) {
            System.out.println(a.extraInfo());
        }
    }
}