# Data Transfer Objects:
import atexit
import os
import sqlite3


class Employee:
    def __init__(self, id, name, salary, coffee_stand):
        self.id = id
        self.name = name
        self.salary = salary
        self.coffee_stand = coffee_stand


class Supplier:
    def __init__(self, id, name, contact_information):
        self.id = id
        self.name = name
        self.contact_information = contact_information


class Product:
    def __init__(self, id, description, price, quantity):
        self.id = id
        self.description = description
        self.price = price
        self.quantity = quantity


class Activity:
    def __init__(self, product_id, quantity, activator_id, date):
        self.product_id = product_id
        self.quantity = quantity
        self.activator_id = activator_id
        self.date = date


class Coffee_stand:
    def __init__(self, id, location, number_of_employees):
        self.id = id
        self.location = location
        self.number_of_employees = number_of_employees


# _________________________________________________________________________________________________________________#

# Data Access Objects: DAO
# All of these are meant to be singletons
class _Employees:
    def __init__(self, conn):
        self._conn = conn

    def insert(self, employee):
        self._conn.execute("""
               INSERT INTO employees (id, name, salary, coffee_stand) VALUES (?, ?, ?, ?)
           """, [employee.id, employee.name, employee.salary, employee.coffee_stand])

    def find(self, employee_id):  # FIND EMPLOYEE BY ID
        c = self._conn.cursor()
        c.execute("""
            SELECT id, name, salary, coffee_stand FROM employees WHERE id = ?
        """, [employee_id])

        return Employee(*c.fetchone())


class _Suppliers:
    def __init__(self, conn):
        self._conn = conn

    def insert(self, supplier):
        self._conn.execute("""
                INSERT INTO suppliers (id, name, contact_information) VALUES (?, ?, ?)
        """, [supplier.id, supplier.name, supplier.contact_information])

    def find(self, supplier_id):  # FIND SUPPLIER BY ID
        c = self._conn.cursor()
        c.execute("""
                SELECT id, name, contact_information FROM suppliers WHERE id = ?
            """, [supplier_id])

        return Supplier(*c.fetchone())


class _Products:
    def __init__(self, conn):
        self._conn = conn

    def insert(self, product):
        self._conn.execute("""
            INSERT INTO products (id, description, price, quantity) VALUES (?, ?, ?, ?)
        """, [product.id, product.description, product.price, product.quantity])

    def find(self, id):
        c = self._conn.cursor()
        foundprod = c.execute("""
            SELECT id, description, price, quantity FROM products WHERE id = ?
        """, [id]).fetchone()
        return Product(*foundprod)

    def update(self,id,quantity):
        self._conn.execute("""
                            UPDATE products SET quantity = (?)
                            WHERE id = (?)
                    """,[quantity,id])


class _Coffee_stands:
    def __init__(self, conn):
        self._conn = conn

    def insert(self, coffee_stand):
        self._conn.execute("""
            INSERT INTO coffee_stands (id, location, number_of_employees) VALUES (?, ?, ?)
        """, [coffee_stand.id, coffee_stand.location, coffee_stand.number_of_employees])

    def find(self, id):
        c = self._conn.cursor()
        all = c.execute("""
            SELECT id, location, number_of_employees FROM coffee_stands WHERE id=?
        """, [id]).fetchone()
        return Coffee_stand(*all)


class _Activities:
    def __init__(self, conn):
        self._conn = conn

    def insert(self, activity):
        self._conn.execute("""
            INSERT INTO activities (product_id, quantity, activator_id, date) VALUES (?, ?, ?, ?)
        """, [activity.product_id, activity.quantity, activity.activator_id, activity.date])

    def findall(self):
        c = self._conn.cursor()
        all = c.execute("""
            SELECT product_id, quantity, activator_id, date FROM activities
            ORDER BY date

        """).fetchall()

        return [*all]



# The Repository
class _Repository:
    def __init__(self):
        self._conn = sqlite3.connect('moncafe.db')
        self.employees = _Employees(self._conn)
        self.suppliers = _Suppliers(self._conn)
        self.products = _Products(self._conn)
        self.coffee_stands = _Coffee_stands(self._conn)
        self.activities = _Activities(self._conn)

    def return_conn(self):
        return self._conn

    def _close(self):
        self._conn.commit()
        self._conn.close()
        # os.remove('moncafe.db')  # DELETE THIS,ONLY FOR DEBUG!!@!#$^#$^@(tral)

    def create_tables(self):
        self._conn.executescript("""
        CREATE TABLE employees (
            id INTEGER PRIMARY KEY,
            name TEXT NOT NULL,
            salary REAL NOT NULL,
            coffee_stand INTEGER REFERENCES coffee_stands(id)
        );

        CREATE TABLE suppliers (
            id INTEGER PRIMARY KEY,
            name TEXT NOT NULL,
            contact_information TEXT
        );

        CREATE TABLE coffee_stands (
            id INTEGER PRIMARY KEY,
            location TEXT NOT NULL,
            number_of_employees INTEGER
        );

        CREATE TABLE products (
            id INTEGER PRIMARY KEY,
            description TEXT NOT NULL,
            price REAL NOT NULL,
            quantity INTEGER NOT NULL
        );
        
        CREATE TABLE activities (
            product_id INTEGER REFERENCES products(id),
            quantity INTEGER NOT NULL,
            activator_id INTEGER NOT NULL,
            date DATE NOT NULL
        );

        """)
    def activities_report(self):
        cursor = self._conn.cursor()
        cursor.execute("""
                SELECT a.date, p.description , a.quantity, e.name, s.name
                FROM activities a 
                JOIN products p ON p.id = a.product_id
                LEFT JOIN  suppliers s ON a.activator_id = s.id
                LEFT JOIN  employees e ON  a.activator_id = e.id
                ORDER BY a.date
                """)
        lines = cursor.fetchall()
        return lines

    def employees_report(self):
        cursor = self._conn.cursor()
        cursor.execute("""
        SELECT * FROM employees ORDER BY employees.name
        """)
        employees = cursor.fetchall()
        totalincome = []
        for emp in employees:
            cursor.execute("""
            SELECT a.product_id, a.quantity , p.price
            FROM activities a, products p
            WHERE a.activator_id = ? AND a.product_id = p.id 
            """, [emp[0]])
            lines= cursor.fetchall()
            totinc = 0
            if lines != []:
                for line in lines:
                    totinc=totinc+(line[1] * -line[2])
            print(str(emp[1]) + ", " + str(emp[2]) + ", " + repo.coffee_stands.find(emp[3]).location + ", " + str(
                    totinc))


# the repository singleton
repo = _Repository()
atexit.register(repo._close)
