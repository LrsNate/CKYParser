JC = javac
JFLAGS = -d ../bin
RM = rm -rf

.SUFFIXES:
.PHONY: all CKYParser TrainSetReader BackConverter AnnotationStripper clean fclean

all: CKYParser TrainSetReader BackConverter AnnotationStripper

CKYParser:
	cd CKYParser/src;\
	$(JC) $(JFLAGS) main/*.java;\
	cd ../bin;\
	echo "Main-Class: main.Main" > MANIFEST.MF;\
	jar cvmf MANIFEST.MF CKYParser.jar main/*.class;\
	cp CKYParser.jar ../..

TrainSetReader:
	cd TrainSetReader/src;\
	$(JC) $(JFLAGS) main/*.java;\
	cd ../bin;\
	echo "Main-Class: main.Main" > MANIFEST.MF;\
	jar cvmf MANIFEST.MF TrainSetReader.jar main/*.class;\
	cp TrainSetReader.jar ../..

BackConverter:
	cd TrainSetReader/src;\
	$(JC) $(JFLAGS) main/*.java;\
	cd ../bin;\
	echo "Main-Class: main.MainBackConversion" > MANIFEST.MF;\
	jar cvmf MANIFEST.MF BackConverter.jar main/*.class;\
	cp BackConverter.jar ../..

AnnotationStripper:
	cd TrainSetReader/src;\
	$(JC) $(JFLAGS) main/*.java;\
	cd ../bin;\
	echo "Main-Class: main.MainAnnotationStripper" > MANIFEST.MF;\
	jar cvmf MANIFEST.MF AnnotationStripper.jar main/*.class;\
	cp AnnotationStripper.jar ../..

clean:
	find . -name MANIFEST.MF | xargs $(RM)
	find . -name "*.class" | xargs $(RM)

fclean: clean
	find . -name "*.jar" | xargs $(RM)