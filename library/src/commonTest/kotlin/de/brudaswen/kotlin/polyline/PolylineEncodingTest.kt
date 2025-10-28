package de.brudaswen.kotlin.polyline

import kotlin.test.Test
import kotlin.test.assertEquals

internal class PolylineEncodingTest {

    @Test
    fun `encoding and decoding should return original Polyline`() {
        val polyline = Polyline(
            coordinates = listOf(
                Coordinate(0.0, 0.0),
                Coordinate(1.0, 1.0),
                Coordinate(-1.0, -1.0),
                Coordinate(-0.00001, -0.00001),
                Coordinate(0.00001, 0.00001),
                Coordinate(-89.99999, -179.99999),
                Coordinate(89.99999, 179.99999),
                Coordinate(-90.0, -180.0),
                Coordinate(90.0, 180.0),
            ),
        )

        assertEquals(
            expected = polyline,
            actual = PolylineEncoding.decode(PolylineEncoding.encode(polyline)),
        )
    }

    @Test
    fun `decoding and encoding should return original String`() {
        val polyline = """
            smdeH_mwbB@cAM_AMo@Ca@Ic@OYE]G]GYE_@G_@I]G_@Ci@I_@Ic@OYI_@Ia@K_@Ga@M[M[MWKYEa@E]A[@[SKKY
            IYKSI]K]KWI_@KYMUIUGWK[MSMQIWKWGa@EYG_@E[A[M[I]E]MQE[IWMSG[Kc@GYGg@G]Ka@Kc@Ia@Ic@Ig@Em@I
            c@Ca@Ia@I[Gc@Ic@Ic@Ge@Ge@K]Em@Kg@Gs@Mi@Gk@Mm@Og@Gk@Om@Gk@Mi@Q_@Q]S]U_@Ua@W[]WWQQSOOKUIUQ
            WS_@Q_@S[K_@O]Oa@Eq@UYQ_@Qe@UUOWQMG[QIM]KUB_@^YTQJa@Hi@He@Fe@Fe@He@H_@F]L_@Hc@De@F]H_@J_
            @Bc@Jc@F_@H_@D_@?_@Aa@@]D_@F_@PWF[D]D[D[FU@a@@UBa@?a@?[D]@c@JYTe@B_@Do@Lm@Ns@De@Dm@Ls@Hg
            @Ji@Jk@Ha@Hi@Pi@Ee@I_@G]Ga@G_@G[Ia@Ka@I]O]Y[YYYUW_@[UY[WYWYSWUUSUWWWSUUU[YWYYW[WYYUQHQKS
            MU]WUS_@UWYQUOQa@[a@Y[]_@Y]Y[WWQ?SOYSYW_@UUOUAQSIm@SSQ[U[U]WWYWS]UWWSSYQWUUQQQMQSQBUKYO[
            G[Ka@M]Ma@C_@Ma@I_@M]ESIUCUBYAS?SBS@WEOO@[?[KSW@WASEUAUGYCYEWBUNs@Ig@IQHOFWHYHWJYI]WMa@O
            k@Sg@Y]O[MUMUG]MYU]QYQe@Oe@WUM]UUOe@Se@Oa@Qg@Qo@S_@O_@QUMYM_@Mc@a@q@I[Qc@So@Kc@Qa@Se@S]O
            _@Qi@Ke@[i@Q_@MUMYGYQ?QBKVUMYG_@@WIYKUOSQMQSIS[SYOKQ[Ec@K]M]IYCg@Cu@Eo@GY@_@@c@E]G[KYQUU
            [Sc@Sg@Oq@Si@Se@Q_@O_@MUKUM[MOQUO[Sa@Oc@Qe@M_@QYQc@Sc@Si@Se@M_@Qa@Qe@Ug@Sc@Sg@Ug@Ue@Qe@U
            a@Oa@Se@Q]Qe@Oc@Si@Oe@Ua@Qc@Qa@M[Qa@KYKYKWKYQYOSSSSQO[O_@Se@Oa@U]]c@Y_@Mc@SYQYYc@Ee@UYSM
            [g@Ua@Ma@SQOWKiAQa@@[WWQc@S[M]Si@M_@Qc@WOOc@Cm@Q[Wg@Y_@Wo@Mg@Qe@M]Oc@Yi@UUMc@Qi@Wi@G_@
        """.trimIndent().replace("\n", "")

        assertEquals(
            expected = polyline,
            actual = PolylineEncoding.encode(PolylineEncoding.decode(polyline)),
        )
    }

    @Test
    fun `encode and decode custom types`() {
        val polyline = CustomPolyline(
            points = listOf(
                CustomCoordinate(0.0f, 0.0f),
                CustomCoordinate(1.0f, 1.0f),
                CustomCoordinate(-1.0f, -1.0f),
                CustomCoordinate(-0.00001f, -0.00001f),
                CustomCoordinate(0.00001f, 0.00001f),
                CustomCoordinate(-89.99999f, -179.9999f),
                CustomCoordinate(89.99999f, 179.9999f),
                CustomCoordinate(-90.0f, -180.0f),
                CustomCoordinate(90.0f, 180.0f),
            ),
        )

        assertEquals(
            expected = polyline,
            actual = PolylineEncoding.decode(
                polyline = PolylineEncoding.encode(polyline),
                polylineFactory = { coordinates ->
                    CustomPolyline(
                        points = coordinates.toList(),
                    )
                },
                coordinateFactory = { lat, lon ->
                    CustomCoordinate(
                        latitude = lat.toFloat(),
                        longitude = lon.toFloat(),
                    )
                },
            ),
        )
    }

    private data class CustomPolyline(
        val points: List<CustomCoordinate>,
    ) : Polyline {
        override val coordinates: List<CustomCoordinate>
            get() = points
    }

    private data class CustomCoordinate(
        val latitude: Float,
        val longitude: Float,
    ) : Coordinate {
        override val lat: Double
            get() = latitude.toDouble()

        override val lon: Double
            get() = longitude.toDouble()
    }
}
