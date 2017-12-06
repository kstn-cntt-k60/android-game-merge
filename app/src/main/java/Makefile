dir:=kstn/game/logic/playing_event

all: ${dir}/cell/CellMessage.java    \
	${dir}/answer/AnswerMessage.java \
	${dir}/guess/GuessMessage.java \
	${dir}/player/PlayerMessage.java \
	${dir}/room/RoomMessage.java \
	${dir}/PlayingMessage.java

${dir}/answer/AnswerMessage.java: ${dir}/answer/answer_message.proto
	protoc --proto_path=. --java_out=. $<

${dir}/cell/CellMessage.java: ${dir}/cell/cell_message.proto
	protoc --proto_path=. --java_out=. $<

${dir}/guess/GuessMessage.java: ${dir}/guess/guess_message.proto
	protoc --proto_path=. --java_out=. $<

${dir}/player/PlayerMessage.java: ${dir}/player/player_message.proto
	protoc --proto_path=. --java_out=. $<

${dir}/room/RoomMessage.java: ${dir}/room/room_message.proto
	protoc --proto_path=. --java_out=. $<

${dir}/PlayingMessage.java: ${dir}/playing_message.proto
	protoc --proto_path=. --java_out=. $<
