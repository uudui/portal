package com.nx;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.stream.Collectors;

/**
 * Created by neal.xu on 2014/10/20.
 */
public class LambdaTest {
    @Test
    public void l1() {
        String[] atp = {"Rafael Nadal", "Novak Djokovic", "Stanislas Wawrinka", "David Ferrer", "Roger Federer", "Andy Murray", "Tomas Berdych", "Juan Martin Del Potro"};
        List<String> players = Arrays.asList(atp);

//        players.forEach((p) -> System.out.println(p + ";"));
//        players.forEach(System.out::print);
        Comparator<String> stringComparator = (String s1, String s2) -> (s1.charAt(s1.length() - 1) - s2.charAt(s2.length() - 1));

        Arrays.sort(atp, stringComparator);
//        players = Arrays.asList(atp);
        players.forEach(System.out::print);

    }

    @Test
    public void t2() {
        List<Person> javaProgrammers = new ArrayList<Person>() {
            {
                add(new Person("Elsdon", "Jaycob", "Java programmer", "male", 43, 2000));
                add(new Person("Tamsen", "Brittany", "Java programmer", "female", 23, 1500));
                add(new Person("Floyd", "Donny", "Java programmer", "male", 33, 1800));
                add(new Person("Sindy", "Jonie", "Java programmer", "female", 32, 1600));
                add(new Person("Vere", "Hervey", "Java programmer", "male", 22, 1200));
                add(new Person("Maude", "Jaimie", "Java programmer", "female", 27, 1900));
                add(new Person("Shawn", "Randall", "Java programmer", "male", 30, 2300));
                add(new Person("Jayden", "Corrina", "Java programmer", "female", 35, 1700));
                add(new Person("Palmer", "Dene", "Java programmer", "male", 33, 2000));
                add(new Person("Addison", "Pam", "Java programmer", "female", 34, 1300));
            }
        };
        Consumer<Person> giveRaise = p -> p.salary += p.salary / 100 * 5;
        javaProgrammers.forEach((p) -> System.out.printf("%s %s %d; ", p.firstName, p.lastName, p.salary));
        System.out.println();
        System.out.println("Increase salary by 5% to programmers:");
        javaProgrammers.forEach(giveRaise);
        javaProgrammers.forEach((p) -> System.out.printf("%s %s %d; ", p.firstName, p.lastName, p.salary));
        System.out.println();
        System.out.println("print salary > 1500:");
        javaProgrammers.stream().filter(p -> p.salary > 1500).forEach(p -> System.out.printf("%d ,", p.salary));

        System.out.println();
        System.out.println("sort by salary , and limit 3");
        List<Person> collect = javaProgrammers.stream().sorted((p1, p2) -> p1.salary - p2.salary).limit(3).collect(Collectors.toList());
        collect.forEach(p -> System.out.printf("%d ,", p.salary));

        System.out.println();
        System.out.println("test map:");
        javaProgrammers.stream().mapToInt(Person::getAge).forEach(p -> System.out.printf("%d ,", p));

        Integer max1 = javaProgrammers.stream().map(Person::getAge).max((p1, p2) -> p1.compareTo(p2)).get();
        Integer max2 = javaProgrammers.stream().mapToInt(Person::getAge).max().getAsInt();
        Assert.assertEquals(max1, max2);

        System.out.println();
        System.out.println("Age List:");
        javaProgrammers.stream().map(Person::getAge).forEach(p -> System.out.printf("%d ,", p));
        System.out.println();
        Integer reduce = javaProgrammers.stream().map(Person::getAge).limit(3).reduce(0, (a, b) -> {
            System.out.printf("a:%d b:%d,", a, b);
            return a + b;
        });
        System.out.printf("reduct is %d", reduce);

        System.out.println();

        String reduce1 = javaProgrammers.stream().map(Person::getFirstName).sorted().reduce("Name:", (a, b) -> a + b);
        System.out.println(reduce1);

        System.out.println();
        Averager averageCollect = javaProgrammers.stream().map(Person::getAge).collect(Averager::new, Averager::accept, Averager::combine);
        System.out.printf("%d %d %f", averageCollect.total, averageCollect.count, averageCollect.average());


        javaProgrammers.stream().collect(Collectors.groupingBy(Person::getGender));


        javaProgrammers.stream().collect(Collectors.groupingBy(Person::getGender, Collectors.mapping(Person::getAge, Collectors.toSet())));


        javaProgrammers.stream().collect(Collectors.groupingBy(Person::getGender, Collectors.averagingInt(Person::getAge)));
        javaProgrammers.stream().collect(Collectors.groupingBy(Person::getGender, Collectors.reducing(0, Person::getAge, (a, b) -> a + b)));
        javaProgrammers.stream().collect(Collectors.groupingBy(Person::getGender, Collectors.reducing(0, Person::getAge, Integer::sum)));

        System.out.println();
    }


    class Person {

        public String firstName, lastName, job, gender;
        public int salary, age;

        public Person(String firstName, String lastName, String job, String gender, int age, int salary) {
            this.firstName = firstName;
            this.lastName = lastName;
            this.gender = gender;
            this.age = age;
            this.job = job;
            this.salary = salary;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }


        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public int getSalary() {
            return salary;
        }

        public void setSalary(int salary) {
            this.salary = salary;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }
    }


    class Averager implements IntConsumer {
        private int total = 0;
        private int count = 0;

        public double average() {
            return count > 0 ? ((double) total) / count : 0;
        }

        @Override
        public void accept(int i) {
            total += i;
            count++;
        }

        public void combine(Averager other) {
            total += other.total;
            count += other.count;
        }
    }
}


