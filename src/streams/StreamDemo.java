package streams;

import jdk.nashorn.internal.runtime.regexp.joni.ast.StringNode;

import java.util.*;
import java.util.stream.Collectors;

public class StreamDemo {
    public static void main(String[] args) {
        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee("kohli", 22));
        employees.add(new Employee("yuvraj", 11));
        employees.add(new Employee("rohit", 33));
        employees.add(new Employee("rahul", 77));
        employees.add(new Employee("dhoni", 44));
        employees.add(new Employee("ashwin", 99));
        employees.add(new Employee("jadeja", 66));
        employees.add(new Employee("dhawan", 33));
        employees.add(new Employee("pandya", 77));

        // sort by salary
        employees.stream().sorted((e1, e2) -> e1.salary - e2.salary).forEach(e -> System.out.println(e.name + " " + e.salary));

        System.out.println("-----------------------");
        // sort by salary and top 3 elements
        employees.stream().sorted((e1, e2) -> e1.salary - e2.salary).limit(3).forEach(e -> System.out.println(e.name + " " + e.salary));

        System.out.println("-----------------------");
        // Collect the same result in list and then print
        List<Employee> employeeList = employees.stream().sorted((e1, e2) -> e1.salary - e2.salary).limit(3).collect(Collectors.toList());
        employeeList.forEach(employee -> System.out.println(employee.name + " " + employee.salary));

        System.out.println("-----------------------");
        // sort on reversed order
        employees.stream().sorted((e1, e2) -> e2.salary - e1.salary).limit(3).forEach(e -> System.out.println(e.name + " " + e.salary));

        System.out.println("-----------------------");
        // another way to do same as above
        employees.stream().sorted(Comparator.comparingInt(Employee::getSalary).reversed()).limit(3).forEach(e -> System.out.println(e.name + " " + e.salary));

        System.out.println("-----------------------");
        // print only name if salary greater than 50 and sort in reversed order
        employees.stream().filter(e -> e.salary > 50).sorted((e1, e2) -> e2.salary - e1.salary).map(e -> e.getName()).forEach(System.out::println);

        // Collect as List
        System.out.println("-----------------------");
        List<String> list = employees.stream().filter(e -> e.salary > 50).sorted((e1, e2) -> e2.salary - e1.salary).map(e -> e.getName()).collect(Collectors.toList());
        System.out.println(list);

        // Collect as Set
        System.out.println("-----------------------");
        Set<String> set = employees.stream().filter(e -> e.salary > 50).map(e -> e.getName()).collect(Collectors.toSet());
        System.out.println(set);

        // Collect as Map
        System.out.println("-----------------------");
        Map<String, Integer> map = employees.stream().filter(e -> e.salary > 50).collect(Collectors.toMap(e -> e.name, e -> e.salary));
        System.out.println(map);

        System.out.println("-----------------------");
        // Join string with delimiter
        String names = employees.stream().limit(3).map(e -> e.getName()).collect(Collectors.joining(", "));
        System.out.println(names);

        // Group by salary
        System.out.println("-----------------------");
        Map<Integer, List<Employee>> empBySalary = employees.stream().collect(Collectors.groupingBy(e -> e.salary));

        for(Map.Entry<Integer, List<Employee>> entry : empBySalary.entrySet()){
            System.out.println(entry.getKey() + " : " + entry.getValue().size());
        }

        System.out.println("-----------------------");
        // Count employees in each salary value
        Map<Integer, Long> empBySalaryCount = employees.stream().collect(Collectors.groupingBy(e -> e.salary, Collectors.counting()));
        for(Map.Entry<Integer, Long> entry : empBySalaryCount.entrySet()){
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

    }
}
