package com.kkumgeurimi.kopring.domain.career.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.kkumgeurimi.kopring.api.dto.CareerBubbleMapResponse
import com.kkumgeurimi.kopring.api.dto.CareerNodeResponse
import com.kkumgeurimi.kopring.domain.career.entity.CareerAnalysis
import com.kkumgeurimi.kopring.domain.career.repository.CareerAnalysisRepository
import com.kkumgeurimi.kopring.domain.student.service.AuthService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class CareerAnalysisService(
    private val careerAnalysisRepository: CareerAnalysisRepository,
    private val authService: AuthService,
    private val objectMapper: ObjectMapper
) {

    @Transactional(readOnly = true)
    fun getCurrentStudentCareerBubbleMap(): CareerBubbleMapResponse? {
        val currentStudent = authService.getCurrentStudent()
        val analysis = careerAnalysisRepository.findTopByStudentOrderByCreatedAtDesc(currentStudent)
        
        return analysis?.let { 
            val nodes = mutableListOf<CareerNodeResponse>()
            
            // 각 노드를 파싱하여 응답 생성
            analysis.node1?.let { nodeJson ->
                parseNodeFromJson(nodeJson, "node1")?.let { nodes.add(it) }
            }
            analysis.node2?.let { nodeJson ->
                parseNodeFromJson(nodeJson, "node2")?.let { nodes.add(it) }
            }
            analysis.node3?.let { nodeJson ->
                parseNodeFromJson(nodeJson, "node3")?.let { nodes.add(it) }
            }
            analysis.node4?.let { nodeJson ->
                parseNodeFromJson(nodeJson, "node4")?.let { nodes.add(it) }
            }
            
            CareerBubbleMapResponse(
                nodes = nodes,
                createdAt = analysis.createdAt?.toString(),
                modifiedAt = analysis.modifiedAt?.toString()
            )
        }
    }


    private fun parseNodeFromJson(nodeJson: String, nodeId: String): CareerNodeResponse? {
        return try {
            val nodeMap = objectMapper.readValue(nodeJson, Map::class.java) as Map<String, Any?>
            
            CareerNodeResponse(
                nodeId = nodeId,
                nodeName = nodeMap["nodeName"]?.toString() ?: "",
                programIds = (nodeMap["programIds"] as? List<*>)?.mapNotNull { it?.toString() }
            )
        } catch (e: Exception) {
            null
        }
    }
}
