CXX := g++
CXXFLAGS := -O3 -march=native -mtune=native -std=c++17 -Wall -Wextra -Wconversion -Wshadow -pedantic
LDFLAGS := -pthread

TARGET := primefinder
SRC := $(wildcard src/*.cpp)
OBJ := $(SRC:.cpp=.o)

all: $(TARGET)

$(TARGET): $(OBJ)
	$(CXX) $(CXXFLAGS) -o $@ $^ $(LDFLAGS)

%.o: %.cpp
	$(CXX) $(CXXFLAGS) -c $< -o $@

run: $(TARGET)
	./$(TARGET)

clean:
	rm -f $(OBJ) $(TARGET)

.PHONY: all run clean
