package streams;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamDemo {
    public static void main(String[] args) {
        int[] numbers = {4, 1, 13, 90, 16, -2, 0};

        // find minimum number
        int min = IntStream.of(numbers).min().getAsInt(); // min() will return OptionalInt
        System.out.println(min); // -2

        // also first we can check ifPresent
        IntStream.of(numbers).min().ifPresent(minimum -> System.out.println(minimum)); // -2

        // Replace lambda with static method reference
        IntStream.of(numbers).min().ifPresent(System.out::println); // -2

        // Others function too
        IntStream.of(numbers).max().ifPresent(System.out::println); // 90
        IntStream.of(numbers).average().ifPresent(System.out::println); // 17.4285 ....
        System.out.println(IntStream.of(numbers).count()); // 7
        System.out.println(IntStream.of(numbers).sum()); // 122

        // Java 8, also provides Single call for all statistics
        IntSummaryStatistics statistics = IntStream.of(numbers).summaryStatistics(); // create stream once
        System.out.println(statistics.getMin()); // -2
        System.out.println(statistics.getMax()); // 90
        System.out.println(statistics.getAverage()); // 17.4285 ....
        System.out.println(statistics.getCount()); // 7
        System.out.println(statistics.getSum()); // 122

        int[] arr = {4, 1, 13, 1, 0, 2, 16, 90};
        // method chaining for complicated logic
        // check here is original array is not mutated
        IntStream.of(arr).distinct().sorted().limit(3).forEach(System.out::println); // 0 1 2
        System.out.println(IntStream.of(arr).sorted().limit(3).sum()); // 2, (since it is not distinct here, so final array is 0, 1, 1)
        System.out.println(IntStream.of(arr).sorted().limit(3).average().getAsDouble()); // 0.66666...
        System.out.println(IntStream.of(arr).sorted().limit(3).count()); // 3
        System.out.println(IntStream.of(arr).sorted().limit(3).min().getAsInt()); // 0
        System.out.println(IntStream.of(arr).sorted().limit(3).max().getAsInt()); // 1

        // some more example
        IntStream.of(numbers).distinct(); // distinct
        IntStream.of(numbers).sorted(); // sort
        IntStream.of(numbers).limit(3); // get first 3
        IntStream.of(numbers).skip(3); // skip first 3
        IntStream.of(numbers).filter(num -> num % 2 == 0); // only even
        IntStream.of(numbers).map(num -> num * 2); // double each num
        IntStream.of(numbers).boxed(); // convert each primitive int to Wrapper Integer

        IntStream.range(1, 100).forEach(System.out::println); // print 1 to 99
        IntStream.range(1, 100).toArray(); // collect into array
        IntStream.range(1, 100).boxed().collect(Collectors.toList()); // collect into list

        IntStream.of(numbers).anyMatch(num -> num % 2 == 1); // is any num odd present
        IntStream.of(numbers).allMatch(num -> num % 2 == 1); // are all num odd

        // Names 3 highest earning employees
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

        employees.stream().sorted((e1, e2) -> e2.salary - e1.salary).limit(3).forEach(e -> System.out.println(e.name)); // ashwin, rahul, jadeja

        // Collectors i.e. collect to list, set, map, string

        // to List
        List<String> listOfEmps = employees.stream().limit(3).map(emp -> emp.name).collect(Collectors.toList());
        System.out.println(listOfEmps);

        // to Set
        Set<String> setOfEmps = employees.stream().limit(3).map(emp -> emp.name).collect(Collectors.toSet());
        System.out.println(setOfEmps);

        // to Map
        Map<String, Employee> mapOfEmps = employees.stream().limit(3).collect(Collectors.toMap(emp -> emp.name, emp -> emp));
        System.out.println(mapOfEmps);

        // to String
        String names = employees.stream().limit(3).map(emp -> emp.name).collect(Collectors.joining(", "));
        System.out.println(names);

        // group by salary
        Map<Integer, List<Employee>> empBySal = employees.stream().collect(Collectors.groupingBy(emp -> emp.salary));
        System.out.println(empBySal);

        // Generally all streams do sequentially, but for doing it parallely, we can use parallel (when we have more items in list)
        Map<Integer, List<Employee>> empBySal1 = employees.stream().parallel().collect(Collectors.groupingBy(emp -> emp.salary));
        System.out.println(empBySal1);
    }
}


class Employee {
    String name;
    int salary;

    public Employee(String name, int salary) {
        this.name = name;
        this.salary = salary;
    }
}