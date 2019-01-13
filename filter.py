import sys, re, os

eventsFile = open(sys.argv[1], "r")
events = [e.split("=")[0] for e in eventsFile.readlines() if "#" not in e]
eventsFile.close()

def isValid(s, events):
    for event in events:
        if "\"f0\":\""+event+"\",\"fs0\":\"O\"" in s:
            return True
        if "\"f0\":\""+event+"\",\"fs0\":\"R\"" in s:
            return True
    
    return False

def getFilteredWarnings(filename):
    warningsFile = open(filename, "r", encoding = "ISO-8859-1")
    warnings = warningsFile.readlines()[0].replace("\"geometry\":", "\n\"geometry\":").split("\n")
    warningsFile.close()

    #print(len(warnings))

    # filter danger level
    filtered = [w for w in warnings if isValid(w, events)]

    # filter eventy type
    print(len(filtered))
    return filtered

def writeData(path, data):
    regex = "\"coordinates\":(\[\[\[[\d,\[\]\.\-]*\]\]\])"

    f = open("filter/"+path, "w")

    for line in data:
        matchers = re.finditer(regex, line)

        for m in matchers:
            s = m.group(1).split("],[")
            s[0] = s[0][3:]

            l = ""
            for i in s:
                if "]" in i:
                    f.write(l[:-1]+"\n")
                    break
                l += " ".join(i.split(","))
                l += ","
                
    f.close()


for files in os.walk("foreca"):
    subfolder = files[0]
    filenames = files[2]

    for filename in filenames:
        path = subfolder+"/"+filename
        data = getFilteredWarnings(path)

        writeData(subfolder.split("/")[1]+"/"+filename.split(".")[0]+".csv", data)
    
