package com.zerobase.weather.controller;

import com.zerobase.weather.domain.Diary;
import com.zerobase.weather.service.DiaryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class DiaryController {

    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }
    @Operation(summary = "일기 텍스트와 날씨를 이용해서 DB에 일기 저장", description = "이것은 노트")
    @PostMapping("/create/diary")
    void createDiary(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(example = "2023-02-01") LocalDate date,
            @RequestBody String text){

        diaryService.createDiary(date, text);

    }

    @Operation(summary = "선택한 날짜의 모든 일기 데이터를 가져옵니다")
    @GetMapping("/read/diary")
    List<Diary> readDiary(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(example = "2023-02-01") LocalDate date){

        return diaryService.readDiary(date);
    }

    @Operation(summary = "선택한 기간중의 모든 일기 데이터를 가져옵니다")
    @GetMapping("/read/diaries")
    List<Diary> readDiaries(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(example = "2023-02-01") LocalDate startDate,
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(example = "2023-03-01") LocalDate endDate){

        return diaryService.readDiaries(startDate, endDate);
    }

    @Operation(summary = "선택한 기간의 일기를 수정합니다")
    @PutMapping("/update/diary")
    void updateDiary(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @Parameter(example = "2023-02-01") LocalDate date,
            @RequestBody String text){

        diaryService.updateDiary(date, text);
    }

    @Operation(summary = "선택한 기간의 일기를 삭제합니다")
    @DeleteMapping("/delete/diary")
    void deleteDiary(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date){

        diaryService.deleteDiary(date);
    }
}
