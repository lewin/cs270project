# Creates a fake set of classes and a fake set of officers, committee members.
# Gives each fake student a random subset of classes.
# Students are free during all other times, and have randomly generated 
# preferences for timeslots, offices, and adjacent slots.

from random import choice, randint

NUM_OFFICES = 2
NUM_DAYS = 5
HOURS_PER_DAY = 6 # Must be at least 2
NUM_TIMESLOTS = NUM_OFFICES * NUM_DAYS * HOURS_PER_DAY
NUM_COURSES = NUM_TIMESLOTS # For simplicity
START_HOUR = 11 # Starts at 11am; this program uses 24-hour system

NUM_OFFICERS = 32
NUM_COMMIES = 5
COURSES_PER_PERSON = 5 # 5 simulates normal courseload + some other obligation

# office: [0, NUM_OFFICES - 1]
# day: [0, NUM_DAYS - 1]
# hour: [0, HOURS_PER_DAY - 1]
def timeslotId(office, day, hour):
    office_offset = office * NUM_DAYS * HOURS_PER_DAY
    day_offset = day * HOURS_PER_DAY
    hour_offset = hour
    return office_offset + day_offset + hour_offset
    
# Make a fake set of courses for fake students to choose from
# On even days (MWF), we have 1 1-hour course per hour
# On odd days (TuTh), we have 1 2-hour course per hour
fake_courses = {} # Course ID's to list of 1-hour time slots
course_ids = [] # List of courses, used to assign students courses
courses_count = 0

# MWF have 1 hour long lectures
for hour in xrange(HOURS_PER_DAY):
    course_id = courses_count
    fake_courses[course_id] = []
    for day in xrange(NUM_DAYS):
        if day % 2 == 0:
            fake_courses[course_id].append((day, hour))
    course_ids.append(course_id)
    courses_count += 1
    
# TuTh have 1.5 hour long lectures => round up to 2 hour long lectures
for hour in xrange(HOURS_PER_DAY):
    course_id = courses_count
    fake_courses[course_id] = []
    for day in xrange(NUM_DAYS):
        if day % 2 == 1:
            fake_courses[course_id].append((day, hour))
            if hour < HOURS_PER_DAY - 1:
                fake_courses[course_id].append((day, hour+1))
    course_ids.append(course_id)
    courses_count += 1
        
# Generate set of course names
course_names = []
course_counter = 0
for course in xrange(NUM_COURSES / 2):
    course_names.append("CS" + str(course_counter))
    course_counter += 1

for course in xrange(NUM_COURSES - (NUM_COURSES / 2)):
    course_names.append("EE" + str(course_counter))
    course_counter += 1

# Important: Make sure NUM_COURSES > COURSES_PER_PERSON !!!
        
# Generate a set of fake officers / committee members and assign them courses
fake_people = {} # Person name to boolean list of availabilities (by timeslots)
for officer_id in xrange(NUM_OFFICERS):
    officer_name = "Officer" + str(officer_id)
    fake_people[officer_name] = [1]*NUM_TIMESLOTS # 1 = available, 0 = busy
    
    course_count = 0
    while course_count < COURSES_PER_PERSON:
        random_course = choice(course_ids)
        conflict = False
        for day, hour in fake_courses[random_course]:
            if not fake_people[officer_name][day*HOURS_PER_DAY + hour]:
                conflict = True
        if not conflict:
            for day, hour in fake_courses[random_course]:
                for office in xrange(NUM_OFFICES):
                    fake_people[officer_name][(day*HOURS_PER_DAY + hour) + (HOURS_PER_DAY * NUM_DAYS * office)] = 0
            course_count += 1
    
for commie_id in xrange(NUM_COMMIES):
    commie_name = "Commie" + str(commie_id)
    fake_people[commie_name] = [1]*NUM_TIMESLOTS
    
    course_count = 0
    while course_count < COURSES_PER_PERSON:
        random_course = choice(course_ids)
        conflict = False
        for day, hour in fake_courses[random_course]:
            if not fake_people[commie_name][day*HOURS_PER_DAY + hour]:
                conflict = True
        if not conflict:
            for day, hour in fake_courses[random_course]:
                fake_people[commie_name][day*HOURS_PER_DAY + hour] = 0
            course_count += 1    
    
def adjSlotIDs(office, day, hour):
    if hour == 0:
        return [timeslotId(office, day, hour + 1)]
    elif hour == HOURS_PER_DAY - 1:
        return [timeslotId(office, day, hour - 1)]
    else:
        return [timeslotId(office, day, hour - 1), timeslotId(office, day, hour + 1)]
        
days = {0:"Monday", 1:"Tuesday", 2:"Wednesday", 3:"Thursday", 4:"Friday"}
    
# Printing out the JSON
def printTutor(tutor_name, last, officer, file):
    file.write('{\n')
    file.write('"tid":' + str(randint(100, 1000)) + ',\n')
    file.write('"name":"' + tutor_name + '",\n')
    file.write('"timeSlots":' + str(fake_people[tutor_name]) + ',\n')
    office_prefs = [(randint(1,2) if randint(1,2) == 2 else randint(-2,-1)) if avail == 0 else 0 for avail in fake_people[tutor_name]]
    file.write('"officePrefs":' + str(office_prefs) + ',\n')
    courses = [randint(-1, 2) for course in course_names]
    file.write('"courses":' + str(courses) + ',\n')
    file.write('"adjacentPref":' + str(randint(0,1)) + ',\n')
    file.write('"numAssignments":' + str(2 if officer else 1) + '\n')
    
    if last:
        file.write('}\n')
    else:
        file.write('},\n\n')

# Slot tuple = (office, day, hour)
def printSlot(slot_tuple, last, file):
    office = slot_tuple[0]
    day = slot_tuple[1]
    hour = slot_tuple[2]
    file.write('{\n')
    file.write('"sid":' + str(timeslotId(office, day, hour)) + ',\n')
    file.write('"name":"' + str(day) + '",\n')
    file.write('"adjacentSlotIDs":' + str(adjSlotIDs(office, day, hour)) + ',\n')
    courses = [0 if (course_name[0:2] == "EE" and office) else 1 for course_name in course_names]
    file.write('"courses":' + str(courses) + ',\n')
    file.write('"day":"' + days[day] + '",\n')
    file.write('"hour":' + str(START_HOUR + hour) + ',\n')
    file.write('"office":"' + ("SODA" if office else "CORY") + '"\n')
    
    if last:
        file.write('}\n')
    else:
        f.write('},\n\n')

# Print everything to file
f = open('SampleDataSet.json', 'w')
f.write('{\n\n')
f.write('"courseNames":' + str(course_names) + ',\n\n')

f.write('"tutors":[\n')
people_names = [person_name for person_name in fake_people]
for person_name in people_names[:-1]:
    printTutor(person_name, False, person_name[0] == "O", f)
printTutor(people_names[-1], True, person_name[0] == "O", f)
f.write('],\n\n')

f.write('"slots":[\n')
all_slots = [(office, day, hour) for office in xrange(NUM_OFFICES) for day in xrange(NUM_DAYS) for hour in xrange(HOURS_PER_DAY)]
for slot in all_slots[:-1]:
    printSlot(slot, False, f)
printSlot(all_slots[-1], True, f)
f.write(']\n\n')

    
f.write('}')
f.close()